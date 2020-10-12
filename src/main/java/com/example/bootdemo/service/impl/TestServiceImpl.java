package com.example.bootdemo.service.impl;

import com.example.bootdemo.proxy.SysLog;
import com.example.bootdemo.proxy.annotations.Permisions;
import com.example.bootdemo.proxy.annotations.Roles;
import com.example.bootdemo.service.TestService;
import org.springframework.stereotype.Service;

/**
 * @Description: TODO
 * @author: lizhe
 * @date: 2020年10月09日 15:41
 */
@Service
public class TestServiceImpl implements TestService {

    @Override
    @SysLog
    @Permisions
    public void testLogAdvice() {
        System.out.println("日志测试方法1");
        testLogAdvice2();
    }
    @Override
    @SysLog
    @Roles
    public void testLogAdvice2() {
        System.out.println("日志测试方法2");
    }
    @Override
    @Permisions
    public void test3(String name) {
        System.out.println("name: " + name);
    }
}
