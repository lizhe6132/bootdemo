package com.example.bootdemo.proxy;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * @Description: 有关Authorized一类切面的调用模板
 * @author: lizhe
 * @date: 2020年10月12日 10:07
 */
public abstract class AuthorizingMethodInterceptor implements MethodInterceptor {

    public AuthorizingMethodInterceptor(){}

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        // 增强的处理
        this.assertAuthorized(methodInvocation);
        return methodInvocation.proceed();
    }

    protected abstract void assertAuthorized(MethodInvocation methodInvocation) throws AuthorizationException;


}
