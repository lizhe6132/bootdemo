package com.example.bootdemo.webconfig;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MyInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        /*request.getSession().setAttribute("openid", "abdkjsfifji");*/
        JSONObject userBaseInfo = new JSONObject();
        userBaseInfo.put("name", "lizhe");
        userBaseInfo.put("openid", "dwffege");
        JSONObject loginInfo = new JSONObject();
        loginInfo.put("loginName", "lz");
        loginInfo.put("openid", "trtythtjht");
        request.getSession().setAttribute("baseUserInfo", userBaseInfo);
        request.getSession().setAttribute("loginInfo", loginInfo);
        return true;
    }
}
