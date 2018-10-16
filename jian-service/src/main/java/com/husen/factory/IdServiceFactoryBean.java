package com.husen.factory;

import com.husen.provider.impl.IpConfigurableMachineIdProvider;
import com.husen.provider.impl.PropertyMachineIdProvider;
import com.husen.service.impl.IdServiceImpl;
import com.husen.service.intf.IdService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.FactoryBean;

/**
 * Created by HuSen on 2018/10/16 13:40.
 */
@Slf4j
@Setter
public class IdServiceFactoryBean implements FactoryBean<IdService> {
    /**
     * 机器ID生成模式
     */
    public enum Type {
        PROPERTY, IP_CONFIGURABLE
    }

    /**
     * 生成方式->发布模式
     */
    @Getter
    public enum GenMethodType {
        EMBED("嵌入发布模式", 0L),
        CENTRAL_SERVICE("中心服务发布模式", 1),
        REST("Rest发布模式", 2L),
        RESERVED("保留未用", 3L);

        private String name;
        private long value;

        GenMethodType(String name, long value) {
            this.name = name;
            this.value = value;
        }
    }

    /**
     * 版本
     */
    @Getter
    public enum Version {
        DEFAULT("默认", 0L),
        EXPANSION("扩展", 1L);
        private String name;
        private long value;

        Version(String name, long value) {
            this.name = name;
            this.value = value;
        }
    }

    private Type providerType;
    private long machineId;
    private String ips;
    private long genMethod = -1;
    private long type = -1;
    private long version = -1;

    private IdService idService;

    public void init() {
        if (providerType == null) {
            log.error("The type of Id service is mandatory.");
            throw new IllegalArgumentException(
                    "The type of Id service is mandatory.");
        }
        switch (providerType) {
            case PROPERTY:
                idService = constructPropertyIdService(machineId);
                break;
            case IP_CONFIGURABLE:
                idService = constructIpConfigurableIdService(ips);
                break;
        }
    }

    private IdService constructPropertyIdService(long machineId) {
        log.info("Construct Property IdService machineId {}", machineId);
        PropertyMachineIdProvider propertyMachineIdProvider = new PropertyMachineIdProvider();
        propertyMachineIdProvider.setMachineId(machineId);
        IdServiceImpl idServiceImpl;
        if (type != -1) {
            idServiceImpl = new IdServiceImpl(type);
        } else {
            idServiceImpl = new IdServiceImpl();
        }
        idServiceImpl.setMachineIdProvider(propertyMachineIdProvider);
        if (genMethod != -1) {
            idServiceImpl.setGenMethod(genMethod);
        }
        if (version != -1) {
            idServiceImpl.setVersion(version);
        }
        idServiceImpl.init();
        return idServiceImpl;
    }

    private IdService constructIpConfigurableIdService(String ips) {
        log.info("Construct Ip Configurable IdService ips {}", ips);
        IpConfigurableMachineIdProvider ipConfigurableMachineIdProvider = new IpConfigurableMachineIdProvider(ips);
        IdServiceImpl idServiceImpl;
        if (type != -1) {
            idServiceImpl = new IdServiceImpl(type);
        } else {
            idServiceImpl = new IdServiceImpl();
        }
        idServiceImpl.setMachineIdProvider(ipConfigurableMachineIdProvider);
        if (genMethod != -1) {
            idServiceImpl.setGenMethod(genMethod);
        }
        if (version != -1) {
            idServiceImpl.setVersion(version);
        }
        idServiceImpl.init();
        return idServiceImpl;
    }

    @Override
    public IdService getObject() throws Exception {
        return idService;
    }

    @Override
    public Class<?> getObjectType() {
        return IdService.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
