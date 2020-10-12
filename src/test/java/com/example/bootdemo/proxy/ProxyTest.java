package com.example.bootdemo.proxy;

import com.example.bootdemo.ApplicationTest;
import com.example.bootdemo.service.TestService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: TODO
 * @author: lizhe
 * @date: 2020年10月09日 10:50
 */
public class ProxyTest extends ApplicationTest {
    @Autowired
    private TestService testService;

    @Test
    public void testProxy() {
        testService.testLogAdvice();
        testService.test3("lizhe1");
    }
}
