package com.husen.timer;

import com.husen.bean.IdMeta;
import com.husen.enums.IdType;

import java.util.Date;

/**
 * Created by HuSen on 2018/10/16 10:20.
 */
public interface Timer {
    long EPOCH = 1514736000000L;

    void init(IdMeta idMeta, IdType idType);

    Date transTime(long time);

    void validateTimestamp(long lastTimestamp, long timestamp);

    long tillNextTimeUnit(long lastTimestamp);

    long genTime();
}
