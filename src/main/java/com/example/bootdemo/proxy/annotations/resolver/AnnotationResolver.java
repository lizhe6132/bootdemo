package com.example.bootdemo.proxy.annotations.resolver;

import org.aopalliance.intercept.MethodInvocation;

import java.lang.annotation.Annotation;

/**
 * @Description: TODO
 * @author: lizhe
 * @date: 2020年10月12日 14:50
 */
public interface  AnnotationResolver {
    public Annotation getAnnotation(MethodInvocation mi, Class<? extends Annotation> clazz);
}
