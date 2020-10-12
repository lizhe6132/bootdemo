package com.example.bootdemo.proxy;

import com.example.bootdemo.proxy.interceptors.AuthorizingAnnotationMethodInterceptor;
import com.example.bootdemo.proxy.interceptors.PermissionAnnotationMethodInterceptor;
import com.example.bootdemo.proxy.interceptors.RoleAnnotationMethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * @Description: TODO
 * @author: lizhe
 * @date: 2020年10月12日 10:27
 */
public abstract class AnnotationsAuthorizingMethodInterceptor extends AuthorizingMethodInterceptor {

    protected Collection<AuthorizingAnnotationMethodInterceptor> methodInterceptors = new ArrayList(2);
    public AnnotationsAuthorizingMethodInterceptor() {
        this.methodInterceptors.add(new RoleAnnotationMethodInterceptor());
        this.methodInterceptors.add(new PermissionAnnotationMethodInterceptor());
    }

    public Collection<AuthorizingAnnotationMethodInterceptor> getMethodInterceptors() {
        return this.methodInterceptors;
    }

    public void setMethodInterceptors(Collection<AuthorizingAnnotationMethodInterceptor> methodInterceptors) {
        this.methodInterceptors = methodInterceptors;
    }
    @Override
    protected void assertAuthorized(MethodInvocation methodInvocation) throws AuthorizationException {
        Collection<AuthorizingAnnotationMethodInterceptor> aamis = this.getMethodInterceptors();
        if (aamis != null && !aamis.isEmpty()) {
            Iterator it = aamis.iterator();

            while(it.hasNext()) {
                AuthorizingAnnotationMethodInterceptor aami = (AuthorizingAnnotationMethodInterceptor)it.next();
                if (aami.supports(methodInvocation)) {
                    aami.assertAuthorized(methodInvocation);
                }
            }
        }

    }
}
