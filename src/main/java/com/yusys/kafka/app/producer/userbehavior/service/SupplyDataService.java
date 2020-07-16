package com.yusys.kafka.app.producer.userbehavior.service;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.yusys.kafka.app.producer.userbehavior.util.AESUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 组织后台数据
 */
@Service
@PropertySource("classpath:userbehavior.properties")
public class SupplyDataService {
    private static final Logger LOG = LoggerFactory.getLogger(SupplyDataService.class);
    @Value("${userUniqueKey}")
    private String userUniqueKey;
    @Value("${userInfoSessionkey}")
    private String userInfoSessionkey;
    @Value("${loginInfoSessionkey}")
    private String loginInfoSessionkey;
    @Value("${serviceAppId}")
    private String serviceAppId;
    @Value("${encryptionFlag}")
    private Boolean encryptionFlag;
    @Value("${encryptKey}")
    private String encryptKey;

    public String supplyData(HttpServletRequest request, String frontData) {
        JSONObject resultJson = new JSONObject();
        JSONObject headJson = new JSONObject();
        String ip = request.getRemoteAddr();
        headJson.put("ip", ip);
        headJson.put("serviceAppId", serviceAppId);
        resultJson.put("header", headJson);
        if (userUniqueKey == null || "".equals(userUniqueKey)) {
            resultJson.put("body", getEncryData(frontData));
            return JSONObject.toJSONString(resultJson);
        }
        HttpSession session = request.getSession();
        if (session == null) {
            resultJson.put("body", getEncryData(frontData));
            return JSONObject.toJSONString(resultJson);
        }
        JSONObject bodyJson = JSONObject.parseObject(frontData);
        Object userBaseInfo = session.getAttribute(userInfoSessionkey);
        bodyJson.put("userBaseInfo", userBaseInfo);
        Object userLoginInfo = session.getAttribute(loginInfoSessionkey);
        bodyJson.put("loginInfo", userLoginInfo);
        Object userUniqueNo = session.getAttribute(userUniqueKey);
        if (userUniqueNo == null && userBaseInfo != null) {
            //尝试从userBaseInfo获取
            try {
                JSONObject baseInfoJson = JSONObject.parseObject(JSONObject.toJSONString(userBaseInfo));
                userUniqueNo = baseInfoJson.get(userUniqueKey);
            } catch (JSONException e) {
                LOG.error("userBaseInfo转json异常{}", e);
            }
        }
        if (userUniqueNo == null && userLoginInfo != null) {
            //尝试从userLoginInfo获取
            try {
                JSONObject logInfoJson = JSONObject.parseObject(JSONObject.toJSONString(userLoginInfo));
                userUniqueNo = logInfoJson.get(userUniqueKey);
            } catch (JSONException e) {
                LOG.error("userLoginInfo转json异常{}", e);
            }
        }
        bodyJson.put("userKey", userUniqueNo);
        resultJson.put("body", getEncryData(bodyJson));
        return JSONObject.toJSONString(resultJson);
    }

    /**
     * 加密处理
     * @param src
     * @return
     */
    private Object getEncryData(Object src) {
        if (encryptionFlag) {
            if (src instanceof java.lang.String) {
                return AESUtil.encrypt(src.toString(), encryptKey);
            }
            if (src instanceof JSONObject) {
                return AESUtil.encrypt(JSONObject.toJSONString(src), encryptKey);
            } else {
                return src;
            }
        } else {
            return src;
        }
    }
}
