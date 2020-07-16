package com.yusys.log4j2.extender.logger.handle;

import com.yusys.log4j2.extender.logger.DesensitizedWords;
import com.yusys.log4j2.extender.logger.MessageHandler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageHandleName implements MessageHandler {
    @Override
    public String postHandle(String tempStr) {
        //姓名正则,\b单词分界
        String nameRegex = "\\b[\\u4E00-\\u9FA5]{2,5}(?:·[\\u4E00-\\u9FA5]{2,5})*\\b";
        Pattern namePattern = Pattern.compile(nameRegex);
        Matcher nameMatcher = namePattern.matcher(tempStr);
        if (nameMatcher.find()) {
            String group0 = nameMatcher.group(0);
            String replacement = DesensitizedWords.desensitize("name", group0);
            tempStr = nameMatcher.replaceAll(replacement);
        }
        return tempStr;
    }
}
