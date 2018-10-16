package com.husen.converter.impl;

import com.husen.bean.Id;
import com.husen.bean.IdMeta;
import com.husen.converter.IdConverter;

/**
 * Created by HuSen on 2018/10/16 9:56.
 */
public class IdConverterImpl implements IdConverter {
    @Override
    public long convert(Id id, IdMeta idMeta) {
        return doConvert(id, idMeta);
    }

    @Override
    public Id convert(long id, IdMeta idMeta) {
        return doConvert(id, idMeta);
    }

    private long doConvert(Id id, IdMeta idMeta) {
        long ret = 0;
        ret |= id.getMachine();
        ret |= id.getSeq() << idMeta.getSeqBitsStartPos();
        ret |= id.getTime() << idMeta.getTimeBitsStartPos();
        ret |= id.getGenMethod() << idMeta.getGenMethodBitsStartPos();
        ret |= id.getType() << idMeta.getTypeBitsStartPos();
        ret |= id.getVersion() << idMeta.getVersionBitsStartPos();
        return ret;
    }

    private Id doConvert(long id, IdMeta idMeta) {
        Id ret = new Id();
        ret.setMachine(id & idMeta.getMachineBitsMask());
        ret.setSeq((id >>> idMeta.getSeqBitsStartPos()) & idMeta.getSeqBitsMask());
        ret.setTime((id >>> idMeta.getTimeBitsStartPos()) & idMeta.getTimeBitsMask());
        ret.setGenMethod((id >>> idMeta.getGenMethodBitsStartPos()) & idMeta.getGenMethodBitsMask());
        ret.setType((id >>> idMeta.getTypeBitsStartPos()) & idMeta.getTypeBitsMask());
        ret.setVersion((id >>> idMeta.getVersionBitsStartPos()) & idMeta.getVersionBitsMask());
        return ret;
    }
}
