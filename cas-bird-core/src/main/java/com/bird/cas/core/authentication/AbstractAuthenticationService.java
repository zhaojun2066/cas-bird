package com.bird.cas.core.authentication;

import com.bird.cas.common.dto.UserPrincipal;
import com.bird.cas.common.query.CheckStQuery;
import com.bird.cas.core.authentication.model.Credentials;
import com.bird.cas.core.authentication.model.Principal;
import com.bird.cas.core.config.CasConfigProperties;
import com.bird.cas.core.exception.CasException;
import com.bird.cas.core.query.LoginQuery;
import com.bird.cas.core.ticket.GTInfo;
import com.bird.cas.core.ticket.STInfo;
import com.bird.cas.core.ticket.SystemClient;
import com.bird.cas.core.ticket.TicketManager;
import com.bird.cas.core.utils.AuthResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @program: cas-bird
 * @description:
 * @author: JuFeng(ZhaoJun)
 * @create: 2021-04-07 14:30
 **/
@Slf4j
public abstract class AbstractAuthenticationService implements Authentication {


    @Autowired
    private TicketManager ticketManager;

    @Autowired
    private CasConfigProperties casConfig;

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 认证之前调用
     * @param request
     * @param response
     */
    public abstract void preAuthenticate(HttpServletRequest request, HttpServletResponse response);


    /**
     * 认证之后调用
     * @param request
     * @param response
     */
    public abstract void afterAuthenticate(HttpServletRequest request,HttpServletResponse response);

    /**
     * 认证出现异常后调用
     * @param request
     * @param exception
     */
    public abstract void afterException(HttpServletRequest request, CasException exception);






    /***
     * 如果出现异常 此方法不会调用
     * 验证成功之后调用的扩展方法
     * @param request
     * @param response
     */
    public abstract void afterAuthenticateSuccess(HttpServletRequest request,HttpServletResponse response);

    /**
     *  登出之前调用
     * @param request
     * @param response
     */
    public abstract void preLogout(HttpServletRequest request,HttpServletResponse response);

    /**
     * 登出删除全局票据之后，执行
     * @param request
     * @param response
     */
    public abstract void afterLogout(HttpServletRequest request,HttpServletResponse response);


    /**
     * 获取callback
     * @param gtInfo
     * @return
     */
    private AuthResult getCallback( GTInfo gtInfo){
        //生成临时票据
        String st = ticketManager.createST(gtInfo);
        return AuthResult.success(st,gtInfo.getGt());
    }




    /**
     * 普通登录验证
     * @param loginQuery
     * @return
     */
    public AuthResult login(LoginQuery loginQuery){
        String requestGt = loginQuery.getGt();
        if (!StringUtils.isEmpty(requestGt)){//检查是否还有效gt
            GTInfo gtInfo = ticketManager.getGTInfoByGT(requestGt);
            if (gtInfo!=null){
                return getCallback(gtInfo);
            }
        }
        // 没有_eventId 动作 ,说明是跳转到登录页面
        String eventId = loginQuery.getEventId();
        if (StringUtils.isEmpty(eventId) || !"submit".equals(eventId)){
            return AuthResult.fail();
        }

        Credentials credentials = loginQuery.getCredentials();
        Principal principal = authenticate(credentials); // 执行用户指定的 验证逻辑
        if (!principal.isPass()){//验证不通过
            return AuthResult.fail();
        }

        GTInfo gt = ticketManager.createGT(principal);
        if (StringUtils.isEmpty(gt.getGt())){
            log.error("服务端错误：create gt is empty");
            throw new CasException("服务端错误");
        }


        return getCallback(gt);
    }


    public void logout(String gt){
        ticketManager.logoutGT(gt);// 注销
        //todo:通知接入系统，session销毁,注意重试，和异步操作

        // 删除gt 关联的 SystemClient
        ticketManager.removeGTSystemClient(gt);
    }


    /**
     * 验证临时票据
     * @param stQuery
     * @return
     */
    public UserPrincipal checkSt(CheckStQuery stQuery){
        UserPrincipal  userPrincipal  = new UserPrincipal();
        String st = stQuery.getSt();
        STInfo stInfo = ticketManager.getSTInfoByST(st);
        if (stInfo!=null){
            stInfo.setSt(st);
            String gt = stInfo.getGt();
            SystemClient systemClient = new SystemClient();
            systemClient.setIp(stQuery.getIp());
            systemClient.setPort(stQuery.getPort());
            systemClient.setSt(st);
            // 关联接入的系统，在gt 失效的时候，通知接入系统销毁session
            ticketManager.addGTSystemClient(gt,systemClient);
            userPrincipal.setId(stInfo.getId());
            userPrincipal.setAttributes(stInfo.getAttributes());
            userPrincipal.setSt(st);
        }
        return userPrincipal;
    }



}
