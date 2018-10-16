package com.husen.populator.impl;

import com.husen.bean.Id;
import com.husen.bean.IdMeta;
import com.husen.populator.base.BasePopulator;
import com.husen.timer.Timer;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by HuSen on 2018/10/16 10:51.
 */
public class AtomicIdPopulator extends BasePopulator {
    private class Variant {
        private long sequence = 0;
        private long lastTimestamp = -1;
    }
    private AtomicReference<Variant> variant = new AtomicReference<>(new Variant());

    public AtomicIdPopulator() {
        super();
    }

    @Override
    public void populateId(Timer timer, Id id, IdMeta idMeta) {
        Variant varOld, varNew;
        long timestamp, sequence;

        while (true) {
            // 保存varOld
            varOld = variant.get();
            //生成varNew
            timestamp = timer.genTime();
            timer.validateTimestamp(varOld.lastTimestamp, timestamp);
            sequence = varOld.sequence;
            if(timestamp == varOld.lastTimestamp) {
                sequence++;
                sequence &= idMeta.getSeqBitsMask();
                if(sequence == 0) {
                    timestamp = timer.tillNextTimeUnit(varOld.lastTimestamp);
                }
            }else {
                sequence = 0;
            }
            varNew = new Variant();
            varNew.sequence = sequence;
            varNew.lastTimestamp = timestamp;

            //cas 如果期待值==varOld，则更新
            if(variant.compareAndSet(varOld, varNew)) {
                id.setSeq(sequence);
                id.setTime(timestamp);
                break;
            }
        }
    }
}
