package com.example.bootdemo.proxy.interceptors;

import com.example.bootdemo.proxy.AuthorizationException;
import com.example.bootdemo.proxy.annotations.handler.AnnotationHandler;
import com.example.bootdemo.proxy.annotations.handler.AuthorizingAnnotationHandler;
import com.example.bootdemo.proxy.annotations.resolver.AnnotationResolver;
import org.aopalliance.intercept.MethodInvocation;

/**
 * @Description: 所有有关权限注解切面的处理父类，要扩展某个权限时继承此类，
 * 然后在AnnotationsAuthorizingMethodInterceptor构造方法中加入此处理类即可
 * @author: lizhe
 * @date: 2020年10月12日 10:29
 */
public abstract class AuthorizingAnnotationMethodInterceptor extends AnnotationMethodInterceptor{

    public AuthorizingAnnotationMethodInterceptor(AnnotationHandler handler) {
        super(handler);
    }

    public AuthorizingAnnotationMethodInterceptor(AnnotationHandler handler, AnnotationResolver resolver) {
        super(handler, resolver);
    }

    public void assertAuthorized(MethodInvocation methodInvocation) {
        try {
            ((AuthorizingAnnotationHandler)this.getHandler()).assertAuthorized(this.getAnnotation(methodInvocation));
        } catch (AuthorizationException e) {
            if (e.getCause() == null) {
                e.initCause(new AuthorizationException("Not authorized to invoke method: " + methodInvocation.getMethod()));
            }

            throw e;
        }

    }
}
