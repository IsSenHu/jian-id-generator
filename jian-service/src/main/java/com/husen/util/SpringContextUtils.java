package com.husen.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Created by HuSen on 2018/8/24 13:54.
 */
@Component("springContextUtils")
public class SpringContextUtils implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextUtils.applicationContext = applicationContext;
    }

    /**
     * 通过name获取 Bean.
     * @param name 实例名称
     * @return Bean
     */
    public static Object getBean(String name) {
        return applicationContext.getBean(name);
    }
}
