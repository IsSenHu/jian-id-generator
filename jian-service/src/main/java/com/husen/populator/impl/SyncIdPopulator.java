package com.husen.populator.impl;

import com.husen.bean.Id;
import com.husen.bean.IdMeta;
import com.husen.populator.base.BasePopulator;
import com.husen.timer.Timer;

/**
 * Created by HuSen on 2018/10/16 10:50.
 */
public class SyncIdPopulator extends BasePopulator {
    public SyncIdPopulator() {
        super();
    }

    @Override
    public synchronized void populateId(Timer timer, Id id, IdMeta idMeta) {
        super.populateId(timer, id, idMeta);
    }
}
