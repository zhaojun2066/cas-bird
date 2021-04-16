package com.bird.cas.client.config;


import com.bird.cas.common.utils.CommonUtils;

import javax.servlet.FilterConfig;
import java.util.ArrayList;
import java.util.List;

import static com.bird.cas.common.PubConstant.CAS_SERVER_DEFAULT_LOGIN_API;


/**
 * @program: cas-bird
 * @description:
 * @author: JuFeng(ZhaoJun)
 * @create: 2020-04-10 14:50
 **/

public enum Conf {
    INSTANCE;
    private String casServerAddress; // 外网域名
    private String localCasServerAddress;// 内网短域名
    private String loginUri;  // 登录地址uri
    private String excludedPaths; // 排除登录的uri
    private int sessionTimeout;// session 失效时间
    private String casServerCheckStUri;// 检查st uri



    public  void init(FilterConfig filterConfig){
        sessionTimeout = (filterConfig.getInitParameter("sessionTimeout")==null || "".equals(filterConfig.getInitParameter("sessionTimeout"))) ? 0 : Integer.valueOf(filterConfig.getInitParameter("sessionTimeout"));
        casServerAddress = filterConfig.getInitParameter("casServerAddress");
        localCasServerAddress = filterConfig.getInitParameter("localCasServerAddress");
        loginUri = filterConfig.getInitParameter("loginUri"); // 默认是/login
        casServerCheckStUri = filterConfig.getInitParameter("casServerCheckStUri"); // 检查st uri
        if(CommonUtils.isNullString(loginUri)){
            loginUri = CAS_SERVER_DEFAULT_LOGIN_API;
        }
        excludedPaths = filterConfig.getInitParameter("excludedPaths");
    }

    public String getCasServerCheckStUri() {
        return casServerCheckStUri;
    }

    public String getLoginUri(){
        return loginUri;
    }

    public String getExcludedPaths() {
        return excludedPaths;
    }

    public String getCasServerAddress() {
        return casServerAddress;
    }

    public String getLocalCasServerAddress() {
        return localCasServerAddress;
    }

    public int getSessionTimeout() {
        return sessionTimeout;
    }
}
