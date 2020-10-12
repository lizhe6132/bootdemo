package com.example.bootdemo.proxy;

import java.lang.annotation.*;

/**
* @Description: 自定义日志注解
* @author: lizhe
* @date: 2020/10/9 9:39
*/
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SysLog {
}
