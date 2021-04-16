package com.bird.cas.client;


import com.alibaba.fastjson.JSONObject;
import com.bird.cas.client.config.Conf;
import com.bird.cas.client.dto.ClientResponse;
import com.bird.cas.common.utils.CommonUtils;
import com.bird.cas.common.utils.ParamUtil;
import com.bird.cas.common.utils.URLEncodeUtil;
import com.bird.cas.session.DistributionSessionRequestWrapper;
import com.bird.cas.session.factory.Factory;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import static com.bird.cas.common.PubConstant.*;

/**
 * @program: cas-bird
 * @description:
 * @author: JuFeng(ZhaoJun)
 * @create: 2021-04-15 17:13
 **/
@Slf4j
public abstract class AbstractCasFilter implements Filter {



    private String casServerAddress;// 单点server地址

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Conf.INSTANCE.init(filterConfig);
        casServerAddress = filterConfig.getInitParameter("casServerAddress");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        DistributionSessionRequestWrapper request = new DistributionSessionRequestWrapper((HttpServletRequest) servletRequest,(HttpServletResponse) servletResponse);
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        preFilter(request,response);

        // 判断是否  不需要进行登录验证
        if (FilterCheck.checkExcludedPaths(request,response)){
            chain.doFilter(request, response);
            return;
        }

        // 验证是否单点服务端过来，进行销毁session的通知
        String destroySt = request.getParameter(DESTROY_ST_PARAM_NAME);
        if (!CommonUtils.isNullString(destroySt)){
            String sessionId =  Factory.getRedisManager().getJedisCluster().get(SESSION_ST_PREFIX+destroySt);
            HttpSession httpSession = Factory.createExitHttpSession(sessionId,request.getServletContext());
            httpSession.invalidate();
            return;
        }

        //验证临时票据 st
        String st = request.getParameter(ST_PARAM_NAME);
        if (!CommonUtils.isNullString(st)){
            boolean checkTicketStatus = FilterCheck.checkTicket(st,request,response);
            if (!checkTicketStatus){
                redirectLogin(request,response);
                return;
            }

            // 如果是前端分离，看看是否需要跳转到前端url页面
            if (FilterCheck.checkBackUrl(request,response)){
                try {
                    // 成功跳转了成功页面，才会去写cookie，其他url 不写，防止sessionId 错乱
                    String backUrl = request.getParameter(BACK_URL_PARAM);
                    if (log.isDebugEnabled()){
                        log.debug("backurl => {}",backUrl);
                    }
                    response.sendRedirect(backUrl);
                    return;
                } catch (IOException e) {
                   // e.printStackTrace();
                }
            }
        }
        else if (!CommonUtils.isNullString(request.getParameter(LOGOUT_REQUEST))){
            FilterCheck.logout(request,response);
            return;
        }

        boolean status = FilterCheck.checkSession(request,response);
        if (!status){
            redirectLogin(request, response);
            return;
        }


        afterFilter(request,response);
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }


    /**
     * 执行filter前执行
     * @param request
     * @param response
     */
    public abstract void preFilter(ServletRequest request, ServletResponse response);


    /**
     *  成功后执行
     * @param request
     * @param response
     */
    public abstract void afterFilter(ServletRequest request, ServletResponse response);




    protected void redirectLogin(HttpServletRequest request, HttpServletResponse response)  {
        try {

            String urlToRedirectTo = getRedirectUrl(request);
            String callback = request.getParameter("callback");
            String requestType = request.getHeader("X-Requested-With");
            ClientResponse result = new ClientResponse();
            result.setStatus(111);
            if ((requestType != null && requestType.toLowerCase().contains("xmlhttprequest"))) {
                if (callback != null) {
                    result.setLocation(urlToRedirectTo);
                    result.setResult(urlToRedirectTo);
                    response.getWriter().print(callback+"("+ JSONObject.toJSONString(result)+")");
                    response.getWriter().flush();
                    return;
                }
                response.setContentType("application/json");
                result.setLocation(urlToRedirectTo);
                result.setResult(urlToRedirectTo);
                response.getWriter().print(JSONObject.toJSONString(result));
                response.getWriter().flush();
            } else {
                /** 跨域请求对预请求的处理，因为预请求不会有X-Requested-With字段头。*/
                requestType = request.getHeader("Access-Control-Request-Headers");
                if (requestType != null && requestType.toLowerCase().contains("x-requested-with")) {
                    response.setContentType("application/json");
                    result.setLocation(urlToRedirectTo);
                    result.setResult(urlToRedirectTo);
                    response.getWriter().print(JSONObject.toJSONString(result));
                    response.getWriter().flush();
                } else {
                    response.sendRedirect(urlToRedirectTo);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    protected String getRedirectUrl(HttpServletRequest request){
        try {
            return casServerAddress+ getLoginUrlWithService(request);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return Conf.INSTANCE.getLoginUri() ;
    }

    protected static String getLoginUrlWithService(HttpServletRequest request) throws UnsupportedEncodingException {
     //   String clientDomain  = Conf.INSTANCE.getClientDomain();
        String serviceUrl = request.getRequestURL().toString();
//        if (!CommonUtils.isNullString(clientDomain)){
//            serviceUrl = clientDomain + request.getRequestURI();
//        }else {
//            serviceUrl = request.getRequestURL().toString();
//
//        }
        String queryString = ParamUtil.getQueryParamsWithoutTicket(request, ST_PARAM_NAME);
        if (!CommonUtils.isNullString(queryString)){
            serviceUrl =  URLEncodeUtil.encode(serviceUrl+"?"+queryString);
        }else {
            serviceUrl = URLEncodeUtil.encode(serviceUrl);
        }
        return Conf.INSTANCE.getLoginUri() + "?" +SERVICE_PARAM_NAME+"="+ serviceUrl;
    }
}
