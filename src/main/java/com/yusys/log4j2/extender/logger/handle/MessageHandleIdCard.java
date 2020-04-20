package com.yusys.log4j2.extender.logger.handle;

import com.yusys.log4j2.extender.logger.DesensitizedWords;
import com.yusys.log4j2.extender.logger.MessageHandler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageHandleIdCard implements MessageHandler {
    @Override
    public String postHandle(String tempStr) {
        //身份证正则,\b单词分界
        String idCardRegex = "\\b[1-9]\\d{5}[1-9]\\d{3}((0[1-9])|(1[0-2]))(([0|1|2][1-9])|3[0-1])((\\d{4})|\\d{3}X)\\b";
        Pattern idCardPattern = Pattern.compile(idCardRegex);
        Matcher idCardMatcher = idCardPattern.matcher(tempStr);
        if (idCardMatcher.find()) {
            String group0 = idCardMatcher.group(0);
            String replacement = DesensitizedWords.desensitize("idCard", group0);
            tempStr = idCardMatcher.replaceAll(replacement);
        }
        return tempStr;
    }
}
