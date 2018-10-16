package com.husen.converter;

import com.husen.bean.Id;
import com.husen.bean.IdMeta;

public interface IdConverter {

    long convert(Id id, IdMeta idMeta);

    Id convert(long id, IdMeta idMeta);

}
