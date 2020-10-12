package com.example.bootdemo.proxy.annotations.resolver;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ClassUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * @Description: TODO
 * @author: lizhe
 * @date: 2020年10月12日 16:18
 */
public class SpringAnnotationResolver implements AnnotationResolver{
    public SpringAnnotationResolver(){}
    @Override
    public Annotation getAnnotation(MethodInvocation mi, Class<? extends Annotation> clazz) {
        Method m = mi.getMethod();
        Annotation a = AnnotationUtils.findAnnotation(m, clazz);
        if (a != null) {
            return a;
        } else {
            Class<?> targetClass = mi.getThis().getClass();
            m = ClassUtils.getMostSpecificMethod(m, targetClass);
            a = AnnotationUtils.findAnnotation(m, clazz);
            return a != null ? a : AnnotationUtils.findAnnotation(mi.getThis().getClass(), clazz);
        }
    }
}
