package com.example.bootdemo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    private static final Logger logger = LoggerFactory.getLogger(TestController.class);
    @RequestMapping("/testLog")
    public Object test() {
        String idCard = "210002197812129527";
        String name = "周星迟数据";
        String name2 = "汉字测试数据";
        String phone = "029-88998899";
        String mobile = "15829241746";
        logger.info("数据:" + idCard);
        logger.info("汉字数据:" + name2);
        logger.info("明文数据:{},{},{},{}", idCard, phone, mobile, name);
        logger.info("idCard:{},phone:{},mobile:{},name:{},idCard:{}", idCard, phone, mobile, name, idCard);
        logger.error("error-idCard:{},error-phone:{},error-mobile:{},error-name:{}", idCard, phone, mobile, name);
        logger.warn("warn-idCard:{},warn-phone:{},warn-mobile:{},warn-name:{}", idCard, phone, mobile, name);
        logger.debug("debug-idCard:{},debug-phone:{},debug-mobile:{},debug-name:{}", idCard, phone, mobile, name);

        return "hello word";
    }
}
