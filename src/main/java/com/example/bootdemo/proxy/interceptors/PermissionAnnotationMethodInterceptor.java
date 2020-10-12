package com.example.bootdemo.proxy.interceptors;

import com.example.bootdemo.proxy.annotations.handler.AnnotationHandler;
import com.example.bootdemo.proxy.annotations.handler.PermisionAnnotationHandler;
import com.example.bootdemo.proxy.annotations.resolver.AnnotationResolver;
import org.aopalliance.intercept.MethodInvocation;

/**
 * @Description: TODO
 * @author: lizhe
 * @date: 2020年10月12日 11:23
 */
public class PermissionAnnotationMethodInterceptor extends AuthorizingAnnotationMethodInterceptor {

    public PermissionAnnotationMethodInterceptor() {
        super(new PermisionAnnotationHandler());
    }

    public PermissionAnnotationMethodInterceptor(AnnotationResolver resolver) {
        super(new PermisionAnnotationHandler(), resolver);
    }

}
