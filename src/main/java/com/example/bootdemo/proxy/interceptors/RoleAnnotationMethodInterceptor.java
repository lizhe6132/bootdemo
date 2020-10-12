package com.example.bootdemo.proxy.interceptors;

import com.example.bootdemo.proxy.annotations.handler.AnnotationHandler;
import com.example.bootdemo.proxy.annotations.handler.RoleAnnotationHandler;
import com.example.bootdemo.proxy.annotations.resolver.AnnotationResolver;
import org.aopalliance.intercept.MethodInvocation;

/**
 * @Description: TODO
 * @author: lizhe
 * @date: 2020年10月12日 11:23
 */
public class RoleAnnotationMethodInterceptor extends AuthorizingAnnotationMethodInterceptor {
    public RoleAnnotationMethodInterceptor() {
        super(new RoleAnnotationHandler());
    }

    public RoleAnnotationMethodInterceptor(AnnotationResolver resolver) {
        super(new RoleAnnotationHandler(), resolver);
    }

}
