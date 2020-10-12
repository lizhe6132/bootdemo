package com.example.bootdemo.proxy.annotations.handler;

import java.lang.annotation.Annotation;

/**
 * @Description: TODO
 * @author: lizhe
 * @date: 2020年10月12日 14:49
 */
public abstract class AnnotationHandler {

    protected Class<? extends Annotation> annotationClass;

    public AnnotationHandler(Class<? extends Annotation> annotationClass) {
        this.setAnnotationClass(annotationClass);
    }


    protected void setAnnotationClass(Class<? extends Annotation> annotationClass) throws IllegalArgumentException {
        if (annotationClass == null) {
            String msg = "annotationClass argument cannot be null";
            throw new IllegalArgumentException(msg);
        } else {
            this.annotationClass = annotationClass;
        }
    }

    public Class<? extends Annotation> getAnnotationClass() {
        return this.annotationClass;
    }
}
