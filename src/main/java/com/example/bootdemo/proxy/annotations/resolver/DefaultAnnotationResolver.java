package com.example.bootdemo.proxy.annotations.resolver;

import org.aopalliance.intercept.MethodInvocation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * @Description: TODO
 * @author: lizhe
 * @date: 2020年10月12日 14:50
 */
public class DefaultAnnotationResolver implements AnnotationResolver {
    @Override
    public Annotation getAnnotation(MethodInvocation mi, Class<? extends Annotation> clazz) {
        if (mi == null) {
            throw new IllegalArgumentException("method argument cannot be null");
        } else {
            Method m = mi.getMethod();
            if (m == null) {
                String msg = MethodInvocation.class.getName() + " parameter incorrectly constructed.  getMethod() returned null";
                throw new IllegalArgumentException(msg);
            } else {
                Annotation annotation = m.getAnnotation(clazz);
                if (annotation == null) {
                    Object miThis = mi.getThis();
                    annotation = miThis != null ? miThis.getClass().getAnnotation(clazz) : null;
                }

                return annotation;
            }
        }
    }
}
