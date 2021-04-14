package com.bird.cas.core.context;

import com.bird.cas.common.PubConstant;
import com.bird.cas.common.model.Param;
import com.bird.cas.common.utils.ParamUtil;
import com.bird.cas.common.utils.URLEncodeUtil;
import com.bird.cas.core.authentication.CredentialsManager;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static com.bird.cas.common.PubConstant.BACK_URL_PARAM;

/**
 * @program: ce-sso
 * @description:
 * @author: JuFeng(ZhaoJun)
 * @create: 2020-04-21 14:22
 **/
public class LoginContextHandlerInterceptorAdapter extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String uri = request.getRequestURI();
        if(uri.contains("/login/")){
            String loginPage = uri.substring(uri.indexOf("/login/")+7);
            LoginContextHandler.setLoginPage("login_"+loginPage);
        }else{
            LoginContextHandler.setLoginPage("login");
        }
        String service = request.getParameter(PubConstant.SERVICE_PARAM_NAME);
        if (!StringUtils.isEmpty(service)){
            String serviceUrl =  URLEncodeUtil.decode(service);
            if (service.contains("backurl")){
                //ww.a.com?backurl
               String  queryUrl = serviceUrl.substring(serviceUrl.indexOf("?")+1);
                List<Param>  params = ParamUtil.getQueryParamList(queryUrl);
                if(params!=null && !params.isEmpty()){
                   for(Param p: params){
                       if (BACK_URL_PARAM.equals(p.getKey())){
                           LoginContextHandler.setBackUrl(p.getValue());
                           break;
                       }
                   }
                }
            }
            String s = serviceUrl.replace("http://","").replace("https://","");
            if (s.contains("/")){
                 s = s.substring(0,s.indexOf("/"));
            }else if(serviceUrl.contains("?")) {
                s = s.substring(0,s.indexOf("?"));
            }
            LoginContextHandler.setService(s);
        }

        LoginContextHandler.setCredentials(CredentialsManager.toCredentials(request));
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        LoginContextHandler.remove();
        super.afterCompletion(request, response, handler, ex);
    }
}
