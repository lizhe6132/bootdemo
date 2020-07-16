package com.yusys.kafka.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringContextHolder implements ApplicationContextAware {
    private static final Logger LOGGER = LoggerFactory.getLogger(SpringContextHolder.class);
    private static ApplicationContext applicationContext;
    @Override
    public void setApplicationContext(ApplicationContext args) throws BeansException {
        SpringContextHolder.applicationContext = args;
    }
    public static <T> T getBean(Class<T> clazz) {
        try {
            return applicationContext.getBean(clazz);
        } catch (BeansException e) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("BeansException, requireType:{},exception:{}", clazz, e);
            }
            return null;
        }
    }

    public static ApplicationContext getApplicationContext(){
        return applicationContext;
    }
}
