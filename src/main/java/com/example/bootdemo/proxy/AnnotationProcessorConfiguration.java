package com.example.bootdemo.proxy;

import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description: 注解配置类
 * @author: lizhe
 * @date: 2020年10月09日 10:25
 */
@Configuration
public class AnnotationProcessorConfiguration {
    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator(){
        return new DefaultAdvisorAutoProxyCreator();
    }
    @Bean
    public LogAdvisor logAdvisor() {
        return new LogAdvisor();
    }
    @Bean
    public AuthorAdvisor authorAdvisor() {
        return new AuthorAdvisor();
    }
}
