package com.husen.populator;

import com.husen.bean.Id;
import com.husen.bean.IdMeta;
import com.husen.timer.Timer;

public interface IdPopulator {
    void populateId(Timer timer, Id id, IdMeta idMeta);
}
