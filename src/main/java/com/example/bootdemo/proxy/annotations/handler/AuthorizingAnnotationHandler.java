package com.example.bootdemo.proxy.annotations.handler;

import com.example.bootdemo.proxy.AuthorizationException;

import java.lang.annotation.Annotation;

/**
 * @Description: TODO
 * @author: lizhe
 * @date: 2020年10月12日 15:13
 */
public abstract class AuthorizingAnnotationHandler extends AnnotationHandler {
    public AuthorizingAnnotationHandler(Class<? extends Annotation> annotationClass) {
        super(annotationClass);
    }

    public abstract void assertAuthorized(Annotation var1) throws AuthorizationException;
}
