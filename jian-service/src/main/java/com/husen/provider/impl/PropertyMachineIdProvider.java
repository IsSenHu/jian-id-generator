package com.husen.provider.impl;

import com.husen.provider.MachineIdProvider;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PropertyMachineIdProvider implements MachineIdProvider {
    private long machineId;
}
