package com.example.bootdemo.proxy;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * @Description: 日志advice
 * @author: lizhe
 * @date: 2020年10月09日 9:43
 */
public class AopLogInterceptor implements MethodInterceptor {
    public AopLogInterceptor(){}
    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        System.out.println("记录日志开始");
        Object proceed = methodInvocation.proceed();
        System.out.println(methodInvocation);
        System.out.println("记录日志结束");
        return proceed;
    }
}
