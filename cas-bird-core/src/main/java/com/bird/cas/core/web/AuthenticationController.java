package com.bird.cas.core.web;

import com.bird.cas.common.PubConstant;
import com.bird.cas.common.dto.UserPrincipal;
import com.bird.cas.common.query.CheckStQuery;
import com.bird.cas.common.utils.CookieUtil;
import com.bird.cas.core.authentication.AbstractAuthenticationService;
import com.bird.cas.core.config.CasConfigProperties;
import com.bird.cas.core.context.LoginContextHandler;
import com.bird.cas.core.query.LoginQuery;
import com.bird.cas.core.ticket.SystemClient;
import com.bird.cas.core.utils.AuthResult;
import com.bird.cas.core.utils.WhiteDomain;
import com.bird.cloud.common.dto.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.bird.cas.common.PubConstant.*;

/**
 * @program: cas-bird
 * @description:
 * @author: JuFeng(ZhaoJun)
 * @create: 2021-04-08 14:18
 **/
@Controller
public class AuthenticationController {

    @Autowired
    private CasConfigProperties casConfig;


    @Autowired
    private AbstractAuthenticationService authenticationService;



    @RequestMapping("/")
    public String index(){
        return "redirect:/login";
    }

    @RequestMapping("/login")
    public String login(HttpServletRequest request, HttpServletResponse response ){
        authenticationService.preAuthenticate(request, response);
        String service = request.getParameter(SERVICE_PARAM_NAME);
        String requestGT = CookieUtil.getValue(request, GT_COOKIE_KEY);
        String eventId = request.getParameter(PubConstant.LOGIN_EVENT_ID_PARAM_NAME);
        LoginQuery query = new LoginQuery(requestGT,service,eventId,LoginContextHandler.getCredentials());
        AuthResult authResult = authenticationService.login(query);
        authenticationService.afterAuthenticate(request, response);
        if (!StringUtils.isEmpty(service)){
            if (service.contains("ticket")){
                service = service.substring(0,service.indexOf("ticket")-1);
            }
            if (!WhiteDomain.white(LoginContextHandler.getService())){
                service = "";
            }
        }

        // ????????????
        if (!authResult.isPass()){
            request.setAttribute("service",service);// ???????????????????????????????????????????????????????????????
            return LoginContextHandler.getLoginPage();
        }

        //??????????????????
        CookieUtil.setCookieWithSameSite(response,request,GT_COOKIE_KEY,authResult.getGt(),PubConstant.GT_COOKIE_PATH,true,casConfig.isCookieSecure());

        // ??????

        authenticationService.afterAuthenticateSuccess(request, response);

        //??????service ??????????????? ??????????????????
        if (StringUtils.isEmpty(service)){
            return PubConstant.LOGIN_SUCCESS_PAGE;
        }


        // ???????????????service?????????302 ???service?????????????????????st???????????????
        //todo: ????????????????????????????????????ip ????????????????????????????????????????????????
        String url = "";
        String st = authResult.getSt();
        if (service.contains("?")){
            url = service+"&ticket="+st;
        }else {
            url = service+"?ticket="+st;
        }

        return "redirect:" + url;


    }

    @RequestMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response ){
        authenticationService.preLogout(request, response);
        String service = request.getParameter(SERVICE_PARAM_NAME);
        String requestGt = CookieUtil.getValue(request,GT_COOKIE_KEY);
        if (!StringUtils.isEmpty(requestGt)){
            removeGtFromCookie(request, response);
        }
        authenticationService.logout(requestGt);
        authenticationService.afterLogout(request, response);
        if (!StringUtils.isEmpty(service)){
            if (service.contains("ticket")){
                service = service.substring(0,service.indexOf("ticket")-1);
            }
            return "redirect:"+service;
        }
        return LOGOUT_SUCCESS_PAGE;
    }


    @PostMapping("/checkST")
    public UserPrincipal checkST(CheckStQuery checkStQuery){
        UserPrincipal userPrincipal = authenticationService.checkSt(checkStQuery);
        return userPrincipal;
    }



    private void  removeGtFromCookie(HttpServletRequest request, HttpServletResponse response){
        String cookie = CookieUtil.getValue(request, GT_COOKIE_KEY);
        if (cookie != null) {
            CookieUtil.remove(request,response,PubConstant.GT_COOKIE_PATH,GT_COOKIE_KEY,casConfig.isCookieSecure());
        }
    }


}
