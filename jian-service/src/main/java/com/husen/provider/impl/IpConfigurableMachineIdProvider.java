package com.husen.provider.impl;

import com.husen.provider.MachineIdProvider;
import com.husen.util.IpUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by HuSen on 2018/10/16 11:12.
 */
@Slf4j
@Getter
@Setter
public class IpConfigurableMachineIdProvider implements MachineIdProvider {
    private long machineId;
    private Map<String, Long> IPS_MAP = new ConcurrentHashMap<>(16);

    public IpConfigurableMachineIdProvider() {
        log.debug("IpConfigurableMachineIdProvider constructed.");
    }

    public IpConfigurableMachineIdProvider(String ips) {
        setIps(ips);
        init();
    }

    private void setIps(String ips) {
        log.debug("IpConfigurableMachineIdProvider ips {}", ips);
        if(!StringUtils.isEmpty(ips)) {
            String[] ipArray = ips.split(",");
            for(int i = 0; i < ipArray.length; i++) {
                IPS_MAP.put(ipArray[i], (long)i);
            }
        }
    }

    private void init() {
        String ip = IpUtils.getHostIp();
        if(StringUtils.isEmpty(ip)) {
            String msg = "Fail to get host IP address. Stop to initialize the IpConfigurableMachineIdProvider provider.";
            log.error(msg);
            throw new IllegalStateException(msg);
        }
        if(!IPS_MAP.containsKey(ip)) {
            String msg = String
                    .format("Fail to configure ID for host IP address %s. Stop to initialize the IpConfigurableMachineIdProvider provider.",
                            ip);
            log.error(msg);
            throw new IllegalStateException(msg);
        }
        machineId = IPS_MAP.get(ip);
        log.info("IpConfigurableMachineIdProvider.init ip {} id {}", ip,
                machineId);
    }
}
