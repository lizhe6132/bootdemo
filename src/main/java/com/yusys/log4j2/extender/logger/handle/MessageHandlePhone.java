package com.yusys.log4j2.extender.logger.handle;

import com.yusys.log4j2.extender.logger.DesensitizedWords;
import com.yusys.log4j2.extender.logger.MessageHandler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageHandlePhone implements MessageHandler {
    @Override
    public String postHandle(String tempStr) {
        //电话号码正则,\b单词分界
        String phoneRegex = "\\b[0][1-9]{2,3}-[0-9]{5,10}\\b";
        Pattern phonePattern = Pattern.compile(phoneRegex);
        Matcher phoneMatcher = phonePattern.matcher(tempStr);
        if (phoneMatcher.find()) {
            String group0 = phoneMatcher.group(0);
            String replacement = DesensitizedWords.desensitize("phone", group0);
            tempStr = phoneMatcher.replaceAll(replacement);
        }
        return tempStr;
    }
}
