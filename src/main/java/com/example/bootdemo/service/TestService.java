package com.example.bootdemo.service;

import com.example.bootdemo.proxy.SysLog;
import org.springframework.stereotype.Component;

/**
 * @Description: TODO
 * @author: lizhe
 * @date: 2020年10月09日 10:27
 */

public interface TestService {
    void testLogAdvice();
    void testLogAdvice2();
    void test3(String name);
}
