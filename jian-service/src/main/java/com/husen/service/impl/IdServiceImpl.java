package com.husen.service.impl;

import com.husen.bean.Id;
import com.husen.populator.IdPopulator;
import com.husen.populator.impl.AtomicIdPopulator;
import com.husen.populator.impl.LockIdPopulator;
import com.husen.populator.impl.SyncIdPopulator;
import com.husen.util.CommonUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by HuSen on 2018/10/16 11:42.
 */
@Slf4j
public class IdServiceImpl extends AbstractIdServiceImpl {
    private static final String SYNC_LOCK_IMPL_KEY = "jian.sync.lock.impl.key";
    private static final String ATOMIC_IMPL_KEY = "jian.atomic.impl.key";

    IdPopulator idPopulator;

    public IdServiceImpl() {
        super();
    }

    public IdServiceImpl(long type) {
        super(type);
    }

    @Override
    public void init() {
        super.init();
        initPopulator();
    }

    private void initPopulator() {
        if (idPopulator != null){
            log.info("The " + idPopulator.getClass().getCanonicalName() + " is used.");
        } else if (CommonUtils.isPropKeyOn(SYNC_LOCK_IMPL_KEY)) {
            log.info("The SyncIdPopulator is used.");
            idPopulator = new SyncIdPopulator();
        } else if (CommonUtils.isPropKeyOn(ATOMIC_IMPL_KEY)) {
            log.info("The AtomicIdPopulator is used.");
            idPopulator = new AtomicIdPopulator();
        } else {
            log.info("The default LockIdPopulator is used.");
            idPopulator = new LockIdPopulator();
        }
    }

    @Override
    protected void populateId(Id id) {
        idPopulator.populateId(timer, id, idMeta);
    }
}
