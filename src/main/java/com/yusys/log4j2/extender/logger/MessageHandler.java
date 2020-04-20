package com.yusys.log4j2.extender.logger;

/**
 * 对log4j2日志内容进行处理
 */
public interface MessageHandler {
    String postHandle(String tempStr);
}
