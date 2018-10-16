package com.husen.timer.impl;

import com.husen.bean.IdMeta;
import com.husen.enums.IdType;
import com.husen.timer.Timer;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import java.util.Date;

/**
 * Created by HuSen on 2018/10/16 10:21.
 */
@Slf4j
@Setter
public class SimpleTimer implements Timer {
    protected IdMeta idMeta;
    private IdType idType;
    private long maxTime;
    private long epoch = EPOCH;

    @Override
    public void init(IdMeta idMeta, IdType idType) {
        this.idMeta = idMeta;
        this.maxTime = (1L << idMeta.getTimeBits()) - 1;
        this.idType = idType;
        this.genTime();
        this.timerUsedLog();
    }

    @Override
    public Date transTime(long time) {
        if(idType == IdType.MILLISECONDS) {
            return new Date(time + epoch);
        }else {
            return new Date(time * 1000 + epoch);
        }
    }

    @Override
    public void validateTimestamp(long lastTimestamp, long timestamp) {
        if(timestamp < lastTimestamp) {
            if (log.isErrorEnabled())
                log.error(String
                        .format("Clock moved backwards.  Refusing to generate id for %d second/millisecond.",
                                lastTimestamp - timestamp));

            throw new IllegalStateException(
                    String.format(
                            "Clock moved backwards.  Refusing to generate id for %d second/millisecond.",
                            lastTimestamp - timestamp));
        }
    }

    @Override
    public long tillNextTimeUnit(long lastTimestamp) {
        log.info(String
                .format("Ids are used out during %d. Waiting till next second/millisecond.",
                        lastTimestamp));
        long timestamp = genTime();
        while (timestamp <= lastTimestamp) {
            timestamp = genTime();
        }
        log.info(String.format("Next second/millisecond %d is up.",
                timestamp));
        return timestamp;
    }

    @Override
    public long genTime() {
        long time;
        if(idType == IdType.MILLISECONDS) {
            time = (System.currentTimeMillis() - epoch);
        }else {
            time = (System.currentTimeMillis() - epoch) / 1000;
        }
        return time;
    }

    private void timerUsedLog() {
        Date expirationDate = transTime(maxTime);
        long days = ((expirationDate.getTime() - System.currentTimeMillis()) / (1000 * 60 * 60 * 24));
        log.info("The current time bit length is {}, the expiration date is {}, this can be used for {} days.",
                idMeta.getTimeBits(), expirationDate, days);
    }
}
