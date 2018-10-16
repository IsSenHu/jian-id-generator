package com.husen.provider.impl;

import com.husen.provider.MachineIdProvider;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Created by HuSen on 2018/10/16 11:23.
 */
@Slf4j
@Getter
@Setter
@Component("dbMachineIdProvider")
public class DbMachineIdProvider implements MachineIdProvider {
    private long machineId;
    // TODO
}
