package com.example.bootdemo.proxy.interceptors;

import com.example.bootdemo.proxy.annotations.handler.AnnotationHandler;
import com.example.bootdemo.proxy.annotations.resolver.AnnotationResolver;
import com.example.bootdemo.proxy.annotations.resolver.DefaultAnnotationResolver;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.annotation.Annotation;

/**
 * @Description:
 * @author: lizhe
 * @date: 2020年10月12日 14:47
 */
public abstract class AnnotationMethodInterceptor {
    /**
     * 注解处理器的父类
     */
    private AnnotationHandler handler;

    private AnnotationResolver resolver;

    public AnnotationMethodInterceptor(AnnotationHandler handler) {
        this(handler, new DefaultAnnotationResolver());
    }

    public AnnotationMethodInterceptor(AnnotationHandler handler, AnnotationResolver resolver) {
        if (handler == null) {
            throw new IllegalArgumentException("AnnotationHandler argument cannot be null.");
        } else {
            this.setHandler(handler);
            this.setResolver((AnnotationResolver)(resolver != null ? resolver : new DefaultAnnotationResolver()));
        }
    }

    public AnnotationHandler getHandler() {
        return handler;
    }

    public void setHandler(AnnotationHandler handler) {
        this.handler = handler;
    }

    public AnnotationResolver getResolver() {
        return resolver;
    }

    public void setResolver(AnnotationResolver resolver) {
        this.resolver = resolver;
    }

    public boolean supports(MethodInvocation mi) {
        return this.getAnnotation(mi) != null;
    }

    protected Annotation getAnnotation(MethodInvocation mi) {
        return this.getResolver().getAnnotation(mi, this.getHandler().getAnnotationClass());
    }
}
