package com.example.bootdemo.proxy.annotations.handler;

import com.example.bootdemo.proxy.AuthorizationException;
import com.example.bootdemo.proxy.annotations.Permisions;

import java.lang.annotation.Annotation;

/**
 * @Description: TODO
 * @author: lizhe
 * @date: 2020年10月12日 15:34
 */
public class PermisionAnnotationHandler extends AuthorizingAnnotationHandler{
    public PermisionAnnotationHandler() {
        super(Permisions.class);
    }
    @Override
    public void assertAuthorized(Annotation annotation) throws AuthorizationException {
        if (annotation instanceof Permisions) {
            //TODO
            System.out.println("控制权限");

        }
    }
}
