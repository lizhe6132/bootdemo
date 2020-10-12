package com.example.bootdemo.proxy;

import com.example.bootdemo.proxy.annotations.Permisions;
import com.example.bootdemo.proxy.annotations.Roles;
import org.springframework.aop.support.StaticMethodMatcherPointcutAdvisor;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * @Description: TODO
 * @author: lizhe
 * @date: 2020年10月12日 11:25
 */
public class AuthorAdvisor extends StaticMethodMatcherPointcutAdvisor {
    private final static Class<? extends Annotation>[] ANNOTATION_CLASS = new Class[]{Roles.class, Permisions.class};
    public AuthorAdvisor() {
        setAdvice(new AllianceAnnotationsAuthorizingMethodInterceptor());
    }
    @Override
    public boolean matches(Method method, Class<?> targetClass) {
        Method m = method;
        if (isLogAnnotationPresent(m)) {
            return true;
        }
        if (targetClass != null) {
            try {
                m = targetClass.getMethod(m.getName(), m.getParameterTypes());
                return isLogAnnotationPresent(m) || isLogAnnotationPresent(targetClass);
            } catch (NoSuchMethodException e) {

            }
        }
        return false;
    }

    private boolean isLogAnnotationPresent(Class<?> targetClass) {
        for (Class<? extends Annotation> annclazz: ANNOTATION_CLASS) {
            Annotation annotation = AnnotationUtils.findAnnotation(targetClass, annclazz);
            if (annotation != null) {
                return true;
            }
        }
        return false;
    }

    private boolean isLogAnnotationPresent(Method m) {
        for (Class<? extends Annotation> annclazz: ANNOTATION_CLASS) {
            Annotation annotation = AnnotationUtils.findAnnotation(m, annclazz);
            if (annotation != null) {
                return true;
            }
        }
        return false;
    }
}
