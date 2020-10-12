package com.example.bootdemo.proxy;

import com.example.bootdemo.proxy.annotations.resolver.SpringAnnotationResolver;
import com.example.bootdemo.proxy.interceptors.AuthorizingAnnotationMethodInterceptor;
import com.example.bootdemo.proxy.interceptors.PermissionAnnotationMethodInterceptor;
import com.example.bootdemo.proxy.interceptors.RoleAnnotationMethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: TODO
 * @author: lizhe
 * @date: 2020年10月12日 16:25
 */
public class AllianceAnnotationsAuthorizingMethodInterceptor extends AnnotationsAuthorizingMethodInterceptor {
    public AllianceAnnotationsAuthorizingMethodInterceptor() {
        List<AuthorizingAnnotationMethodInterceptor> methodInterceptors = new ArrayList<>(2);
        SpringAnnotationResolver resolver = new SpringAnnotationResolver();
        methodInterceptors.add(new RoleAnnotationMethodInterceptor(resolver));
        methodInterceptors.add(new PermissionAnnotationMethodInterceptor(resolver));
        this.setMethodInterceptors(methodInterceptors);
    }
}
