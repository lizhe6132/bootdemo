package com.example.bootdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@ServletComponentScan(basePackages = "com.yusys.log4j2.extender.servlet")
//@ComponentScan(basePackages = {"com.example.bootdemo", "com.yusys.kafka"})
//@EnableAspectJAutoProxy(exposeProxy=true)
public class BootdemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(BootdemoApplication.class, args);
    }

}
