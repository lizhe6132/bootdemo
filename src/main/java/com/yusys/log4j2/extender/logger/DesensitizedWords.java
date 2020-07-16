package com.yusys.log4j2.extender.logger;

public enum DesensitizedWords {
    idCard("idCard", 6, 4),
    phone("phone", 3, 4),
    mobile("mobile", 3, 4),
    name("name", 0, 1),;
    private String word;
    private int front;
    private int tail;

    DesensitizedWords(String word, int front, int tail) {
        this.word = word;
        this.front = front;
        this.tail = tail;
    }

    public static String desensitize(String word, String val) {
        for (DesensitizedWords item : DesensitizedWords.values()) {
            if (word.contains(item.word)) {
                return hide(val, item.front, item.tail, '*');
            }
        }
        return val;
    }

    public static String hide(String src, int front, int tail, char replace) {
        if (null == src)
            return src;
        int len = src.length();
        if (front > len || tail > len) {
            return src;
        }

        StringBuilder builder = new StringBuilder();
        if (front > 0) {
            builder.append(src.substring(0, front));
        } else {
            front = 0;
        }
        String tailStr = "";
        if (tail > 0) {
            tailStr = src.substring(src.length() - tail, src.length());
        } else {
            tail = 0;
        }
        int padding = len - front - tail;
        if (padding > 0) {
            for (int i = 0; i < padding; i++) {
                builder.append(replace);
            }
        }
        builder.append(tailStr);
        return builder.toString();
    }
}
