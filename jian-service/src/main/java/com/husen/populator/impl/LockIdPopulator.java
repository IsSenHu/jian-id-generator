package com.husen.populator.impl;

import com.husen.bean.Id;
import com.husen.bean.IdMeta;
import com.husen.populator.base.BasePopulator;
import com.husen.timer.Timer;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by HuSen on 2018/10/16 10:49.
 */
public class LockIdPopulator extends BasePopulator {
    private Lock lock = new ReentrantLock();

    public LockIdPopulator() {
        super();
    }

    @Override
    public void populateId(Timer timer, Id id, IdMeta idMeta) {
        lock.lock();
        try {
            super.populateId(timer, id, idMeta);
        }finally {
            lock.unlock();
        }
    }
}
