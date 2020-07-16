package com.yusys.log4j2.extender.logger;

import org.apache.logging.log4j.util.PropertiesUtil;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class MatchAndReplaceUtil {
    private static final String DEBUG_REGEX = "\\bDEBUG\\b";
    private static final String DEFAULT_HANDLE = "default.message.handle";
    // 消息处理类集合
    private static List<MessageHandler> messageHandlerList;
    static {
        String value = PropertiesUtil.getProperties().getStringProperty(DEFAULT_HANDLE);
        if (value != null) {
            String[] classNames = StringUtils.commaDelimitedListToStringArray(value);
            messageHandlerList = new ArrayList<>(classNames.length);
            for (String className : classNames) {
                try {
                    Class clazz = Class.forName(className);
                    if (clazz != null) {
                        Object o = clazz.newInstance();
                        if (o instanceof  MessageHandler) {
                            messageHandlerList.add((MessageHandler)o);
                        }
                    }
                }
                catch (Exception ex) {
                    throw new RuntimeException(
                            "Could not find MessageHandler's default strategy class [" + className +
                                    "] for interface MessageHandler", ex);
                }

            }

        }
    }
    /**
     * 我的自定义处理
     * @param buffer
     */
    public static void myPostProcess(final StringBuilder buffer) {
        if (buffer.length() == 0) {
            return;
        }
        String tempStr = buffer.toString();
        // 判断是DEBUG的信息不做处理
        Pattern pattern = Pattern.compile(DEBUG_REGEX);
        Matcher matcher = pattern.matcher(tempStr);
        if (matcher.find()) {
            String group0 = matcher.group(0);
            if ("DEBUG".equals(group0)) {
                return;
            }
        }
        buffer.setLength(0);
        if (messageHandlerList != null && messageHandlerList.size() > 0) {
            for (MessageHandler messageHandler: messageHandlerList) {
                tempStr = messageHandler.postHandle(tempStr);
            }
        }
        buffer.append(tempStr);
    }
}
