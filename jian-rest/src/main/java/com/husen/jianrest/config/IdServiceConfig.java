package com.husen.jianrest.config;

import com.husen.enums.IdType;
import com.husen.factory.IdServiceFactoryBean;
import com.husen.service.intf.IdService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by HuSen on 2018/10/16 13:38.
 */
@Configuration
public class IdServiceConfig {

    @Bean
    public IdService idService() throws Exception {
        IdServiceFactoryBean factory = new IdServiceFactoryBean();
        factory.setGenMethod(IdServiceFactoryBean.GenMethodType.REST.getValue());
        factory.setVersion(IdServiceFactoryBean.Version.DEFAULT.getValue());
        factory.setProviderType(IdServiceFactoryBean.Type.PROPERTY);
        factory.setType(IdType.SECONDS.value());
        factory.setMachineId(1021L);
        factory.init();
        return factory.getObject();
    }
}
