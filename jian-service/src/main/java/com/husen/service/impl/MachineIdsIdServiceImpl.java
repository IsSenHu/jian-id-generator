package com.husen.service.impl;

import com.husen.bean.Id;
import com.husen.populator.ResetPopulator;
import com.husen.provider.MachineIdsProvider;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by HuSen on 2018/10/16 11:48.
 */
@Slf4j
@Service("machineIdsIdServiceImpl")
@Setter
public class MachineIdsIdServiceImpl extends IdServiceImpl {
    private long lastTimestamp = -1L;
    private static final Map<Long, Long> MACHINE_ID_MAP = new ConcurrentHashMap<>(16);
    private static final String STORE_FILE_NAME = "machineIdInfo.store";
    private String storeFilePath;
    private File storeFile;
    private Lock lock = new ReentrantLock();

    @Override
    public void init() {
        if (!(this.machineIdProvider instanceof MachineIdsProvider)) {
            log.error("The machineIdProvider is not a MachineIdsProvider instance so that Vesta Service refuses to start.");
            throw new RuntimeException(
                    "The machineIdProvider is not a MachineIdsProvider instance so that Vesta Service refuses to start.");
        }
        super.init();
        initStoreFile();
        initMachineId();
    }

    @Override
    protected void populateId(Id id) {
        supportChangeMachineId(id);
    }

    private void supportChangeMachineId(Id id) {
        try{
            id.setMachine(machineId);
            idPopulator.populateId(timer, id, idMeta);
            lastTimestamp = id.getTime();
        }catch (IllegalStateException e) {
            log.warn("Clock moved backwards, change MachineId and reset IdPopulator");
            lock.lock();
            try{
                if(id.getMachine() == machineId) {
                    changeMachineId();
                    resetIdPopulator();
                }
            }finally {
                lock.unlock();
            }
        }
    }

    private void resetIdPopulator() {
        if(idPopulator instanceof ResetPopulator) {
            ((ResetPopulator)idPopulator).reset();
        }else {
            try{
                this.idPopulator = this.idPopulator.getClass().newInstance();
            } catch (IllegalAccessException | InstantiationException e) {
                throw new RuntimeException("Reset IdPopulator <[" + this.idPopulator.getClass().getCanonicalName() + "]> instance error", e);
            }
        }
    }

    private void changeMachineId() {
        MACHINE_ID_MAP.put(this.machineId, this.lastTimestamp);
        storeInFile();
        initMachineId();
    }

    private void storeInFile() {
        Writer writer = null;
        try{
            writer = new FileWriter(storeFile, false);
            for(Map.Entry<Long, Long> entry : MACHINE_ID_MAP.entrySet()) {
                writer.write(entry.getKey() + ":" + entry.getValue() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
          if(writer != null) {
              try {
                  writer.close();
              } catch (IOException e) {
                  log.error("close writer id error:{}", e.getMessage());
              }
          }
        }
    }

    private void initMachineId() {
        long startId = this.machineId;
        long newMachineId = this.machineId;
        while (true) {
            if (MACHINE_ID_MAP.containsKey(newMachineId)) {
                long timestamp = timer.genTime();
                if (MACHINE_ID_MAP.get(newMachineId) < timestamp) {
                    this.machineId = newMachineId;
                    break;
                } else {
                    newMachineId = ((MachineIdsProvider) this.machineIdProvider).getNextMachineId();
                }
                if (newMachineId == startId) {
                    throw new RuntimeException("No machineId is available");
                }
                validateMachineId(newMachineId);
            } else {
                this.machineId = newMachineId;
                break;
            }
        }
        log.warn("MachineId: {}", this.machineId);
    }

    private void initStoreFile() {
        if (storeFilePath == null || storeFilePath.length() == 0) {
            storeFilePath = System.getProperty("user.dir") + File.separator + STORE_FILE_NAME;
        }
        try {
            log.info("machineId info store in <[" + storeFilePath + "]>");
            storeFile = new File(storeFilePath);
            if (storeFile.exists()) {
                BufferedReader reader = new BufferedReader(new FileReader(storeFile));
                String line = reader.readLine();
                while (line != null && line.length() > 0) {
                    String[] kvs = line.split(":");
                    if (kvs.length == 2) {
                        MACHINE_ID_MAP.put(Long.parseLong(kvs[0]), Long.parseLong(kvs[1]));
                    } else {
                        throw new IllegalArgumentException(storeFile.getAbsolutePath() + " has illegal value <[" + line + "]>");
                    }
                    line = reader.readLine();
                }
                reader.close();
            }
        } catch (IOException e) {
            log.error("error:{}", e.getMessage());
            e.printStackTrace();
        }
    }
}
