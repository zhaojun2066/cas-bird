package com.bird.cas.client;

import com.alibaba.fastjson.JSONObject;
import com.bird.cas.client.config.Conf;
import com.bird.cas.client.path.impl.AntPathMatcher;
import com.bird.cas.client.utils.HTTPUtil;
import com.bird.cas.client.utils.IP;
import com.bird.cas.common.dto.UserPrincipal;
import com.bird.cas.common.model.Param;
import com.bird.cas.common.query.CheckStQuery;
import com.bird.cas.common.utils.CommonUtils;
import com.bird.cas.common.utils.SignUtil;
import com.bird.cas.common.utils.URLEncodeUtil;
import com.bird.cas.session.DistributionSessionRequestWrapper;
import com.bird.cas.session.factory.Factory;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import static com.bird.cas.common.PubConstant.*;
import static com.bird.cas.common.utils.ParamUtil.getQueryParamList;

/**
 * @program: cas-bird
 * @description:
 * @author: JuFeng(ZhaoJun)
 * @create: 2021-04-15 17:29
 **/
@Slf4j
public class FilterCheck {
    private static final AntPathMatcher antPathMatcher = new AntPathMatcher();

    private static final String excludedPaths = Conf.INSTANCE.getExcludedPaths();

    private static final String localCasServerAddress = Conf.INSTANCE.getLocalCasServerAddress();

    private static final String casServerAddress = Conf.INSTANCE.getCasServerAddress();

    private static final String casServerCheckStUri = Conf.INSTANCE.getCasServerCheckStUri();

    public static boolean checkExcludedPaths(HttpServletRequest request, HttpServletResponse response ){
        String servletPath = request.getServletPath();// 获取请求路径
        if (!CommonUtils.isNullString(excludedPaths)){
            for (String excludedPath:excludedPaths.split(",")){
                String uriPattern = excludedPath.trim();
                if (antPathMatcher.match(uriPattern, servletPath)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean checkTicket(String ticket ,HttpServletRequest request, HttpServletResponse response ){
        String sign = SignUtil.getSign(ticket); // 生成签名
        String ip = IP.getLocalIP();// 获取内网本地ip
        int port = request.getLocalPort(); // 获取服务端端口
        String requestUri = null;
        try {
            requestUri = URLEncodeUtil.encode(request.getRequestURI());
        } catch (UnsupportedEncodingException e) {
            log.error("encode requestUri err {}",e.getMessage());
            return false;
        }
        String casServer = casServerAddress;
        if (!CommonUtils.isNullString(localCasServerAddress)){
            casServer = localCasServerAddress;
        }

        CheckStQuery checkStQuery = new CheckStQuery();
        checkStQuery.setIp(ip);
        checkStQuery.setPort(port);
        checkStQuery.setSt(ticket);
        checkStQuery.setSystemClientRequestURI(requestUri);
        checkStQuery.setSign(sign);


        String requestBody = JSONObject.toJSONString(checkStQuery);
        String responseBody = HTTPUtil.doPostHttpRequest(casServer+casServerCheckStUri,requestBody);
        if (CommonUtils.isNullString(responseBody)){
            return false;
        }

        UserPrincipal userPrincipal = JSONObject.parseObject(responseBody, UserPrincipal.class);
        if (userPrincipal==null || CommonUtils.isNullString(userPrincipal.getId())){
            return false;
        }

        HttpSession session = request.getSession();
        Factory.getRedisManager().getJedisCluster().set(SESSION_ST_PREFIX +ticket,session.getId());
        session.setAttribute(SESSION_USER_KEY,userPrincipal);
        return true;
    }

    public static boolean checkBackUrl(HttpServletRequest request, HttpServletResponse response) {
        String backUrl = request.getParameter(BACK_URL_PARAM);
        if (backUrl!=null && !"".equals(backUrl)){
            return true;
        }
        return false;
    }

    public static void logout(DistributionSessionRequestWrapper request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        if (session!=null){
            session.invalidate(); // 删除session
        }
        // 跳转到登录页面
        String path = null;
        try {
            path = casServerAddress+getLogoutUrl(request);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (path==null){
            path = casServerAddress + CAS_SERVER_LOGOUT_API;
        }
        try {
            response.sendRedirect(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getLogoutUrl(HttpServletRequest request) throws UnsupportedEncodingException {
        String serviceUrl = request.getRequestURL().toString();
        StringBuilder param = new StringBuilder();
        String queryString = request.getQueryString();
        List<Param> params = getQueryParamList(queryString);
        if (!params.isEmpty()){
            for (Param p : params){
                String name = p.getKey();
                if (name.equals(LOGOUT_REQUEST)){
                    continue;
                }
                String value =p.getValue() ;
                param.append(name).append("=").append(value).append("&");
            }
        }
        // 如果有参数
        if (param.length()>0){
            serviceUrl = URLEncodeUtil.encode(serviceUrl+ "?" +param.substring(0,param.toString().lastIndexOf("&")));
        }else {
            serviceUrl = URLEncodeUtil.encode(serviceUrl);
        }
        if (!CommonUtils.isNullString(serviceUrl)){
            return CAS_SERVER_LOGOUT_API +"?loginUri="+Conf.INSTANCE.getLoginUri() + "&"+SERVICE_PARAM_NAME +"="+serviceUrl;
        }
        return CAS_SERVER_LOGOUT_API +"?loginUri="+Conf.INSTANCE.getLoginUri();

    }

    public static boolean checkSession(DistributionSessionRequestWrapper request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        if (session==null){
            return  false;
        }
        UserPrincipal userDto =   (UserPrincipal) session.getAttribute(SESSION_USER_KEY);
        if (userDto==null){
            return false;
        }

        return true;
    }
}
