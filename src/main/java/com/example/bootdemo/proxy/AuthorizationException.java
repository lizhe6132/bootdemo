package com.example.bootdemo.proxy;

/**
 * @Description: 自定义异常便于异常统一处理及做业务判断
 * @author: lizhe
 * @date: 2020年10月12日 10:38
 */
public class AuthorizationException extends RuntimeException {

    public AuthorizationException() {
    }

    public AuthorizationException(String message) {
        super(message);
    }

    public AuthorizationException(Throwable cause) {
        super(cause);
    }

    public AuthorizationException(String message, Throwable cause) {
        super(message, cause);
    }
}
