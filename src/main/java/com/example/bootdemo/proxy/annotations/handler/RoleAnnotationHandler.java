package com.example.bootdemo.proxy.annotations.handler;

import com.example.bootdemo.proxy.AuthorizationException;
import com.example.bootdemo.proxy.annotations.Roles;

import java.lang.annotation.Annotation;

/**
 * @Description: TODO
 * @author: lizhe
 * @date: 2020年10月12日 15:23
 */
public class RoleAnnotationHandler extends AuthorizingAnnotationHandler{
    public RoleAnnotationHandler() {
        super(Roles.class);

    }
    @Override
    public void assertAuthorized(Annotation annotation) throws AuthorizationException {
        if (annotation instanceof Roles) {
            //TODO
            System.out.println("角色权限");
        }

    }
}
