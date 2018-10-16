package com.husen.populator.base;

import com.husen.bean.Id;
import com.husen.bean.IdMeta;
import com.husen.populator.IdPopulator;
import com.husen.populator.ResetPopulator;
import com.husen.timer.Timer;

/**
 * Created by HuSen on 2018/10/16 10:39.
 */
public abstract class BasePopulator implements IdPopulator, ResetPopulator {
    private long sequence = 0;
    private long lastTimestamp = -1;

    public BasePopulator() {
        super();
    }

    @Override
    public void populateId(Timer timer, Id id, IdMeta idMeta) {
        long timestamp = timer.genTime();
        timer.validateTimestamp(lastTimestamp, timestamp);
        // 如果在当前时间段内
        if(timestamp == lastTimestamp) {
            sequence++;
            sequence &= idMeta.getSeqBitsMask();
            // sequence == 0 表示当前时间段内的序列号已用完，等待到达下一个时间段在进行序列号的生成
            if(sequence == 0) {
                timestamp = timer.tillNextTimeUnit(lastTimestamp);
            }
        }
        // 不在当前时间段，则序列号重新从0开始
        else {
            lastTimestamp = timestamp;
            sequence = 0;
        }
        id.setSeq(sequence);
        id.setTime(timestamp);
    }

    @Override
    public void reset() {
        this.sequence = 0;
        this.lastTimestamp = -1L;
    }
}
