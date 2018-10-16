package com.husen.service.impl;

import com.husen.bean.Id;
import com.husen.bean.IdMeta;
import com.husen.converter.IdConverter;
import com.husen.converter.impl.IdConverterImpl;
import com.husen.enums.IdType;
import com.husen.factory.IdMetaFactory;
import com.husen.provider.MachineIdProvider;
import com.husen.service.intf.IdService;
import com.husen.timer.Timer;
import com.husen.timer.impl.SimpleTimer;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.Date;

/**
 * Created by HuSen on 2018/10/16 11:25.
 */
@Slf4j
@Setter
@Service
public abstract class AbstractIdServiceImpl implements IdService {
    private IdConverter idConverter = new IdConverterImpl();
    protected Timer timer = new SimpleTimer();

    protected long machineId = -1;
    protected long genMethod = 0;
    protected long version = 0;

    protected IdType idType;
    protected IdMeta idMeta;
    protected MachineIdProvider machineIdProvider;


    AbstractIdServiceImpl() {
        idType = IdType.SECONDS;
    }

    AbstractIdServiceImpl(long type) {
        idType = IdType.parse(type);
    }

    public void init() {
        if(this.idMeta == null) {
            setIdMeta(IdMetaFactory.getIdMeta(idType));
        }
        this.timer.init(idMeta, idType);
        this.machineId = machineIdProvider.getMachineId();
        validateMachineId(this.machineId);
    }

    @Override
    public long genId() {
        Id id = new Id();
        id.setMachine(machineId);
        id.setGenMethod(genMethod);
        id.setType(idType.value());
        id.setVersion(version);
        populateId(id);
        long ret = idConverter.convert(id, this.idMeta);
        log.trace(String.format("Id: %s => %d", id, ret));
        return ret;
    }

    void validateMachineId(long machineId) {
        if (machineId < 0) {
            log.error("The machine ID is not configured properly (" + machineId + " < 0) so that Vesta Service refuses to start.");
            throw new IllegalStateException(
                    "The machine ID is not configured properly (" + machineId + " < 0) so that Vesta Service refuses to start.");
        } else if (machineId >= (1 << this.idMeta.getMachineBits())) {
            log.error("The machine ID is not configured properly ("
                    + machineId + " >= " + (1 << this.idMeta.getMachineBits()) + ") so that Vesta Service refuses to start.");

            throw new IllegalStateException("The machine ID is not configured properly ("
                    + machineId + " >= " + (1 << this.idMeta.getMachineBits()) + ") so that Vesta Service refuses to start.");
        }
    }

    protected abstract void populateId(Id id);

    @Override
    public Date transTime(long time) {
        return timer.transTime(time);
    }

    @Override
    public Id expId(long id) {
        return idConverter.convert(id, this.idMeta);
    }

    @Override
    public long makeId(long time, long seq) {
        return makeId(time, seq, machineId);
    }

    @Override
    public long makeId(long time, long seq, long machine) {
        return makeId(genMethod, time, seq, machine);
    }

    @Override
    public long makeId(long genMethod, long time, long seq, long machine) {
        return makeId(idType.value(), genMethod, time, seq, machine);
    }

    @Override
    public long makeId(long type, long genMethod, long time, long seq, long machine) {
        return makeId(version, type, genMethod, time, seq, machine);
    }

    @Override
    public long makeId(long version, long type, long genMethod, long time, long seq, long machine) {
        Id id = new Id(machine, seq, time, genMethod, type, version);
        return idConverter.convert(id, this.idMeta);
    }
}
