package com.yusys.log4j2.extender.logger.handle;

import com.yusys.log4j2.extender.logger.DesensitizedWords;
import com.yusys.log4j2.extender.logger.MessageHandler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageHandleMobile implements MessageHandler {
    @Override
    public String postHandle(String tempStr) {
        //手机号码正则,\b单词分界
        String mobileRegex = "\\b[1][3,4,5,7,8][0-9]{9}\\b";
        Pattern mobilePattern = Pattern.compile(mobileRegex);
        Matcher mobileMatcher = mobilePattern.matcher(tempStr);
        if (mobileMatcher.find()) {
            String group0 = mobileMatcher.group(0);
            String replacement = DesensitizedWords.desensitize("mobile", group0);
            tempStr = mobileMatcher.replaceAll(replacement);
        }
        return tempStr;
    }
}
