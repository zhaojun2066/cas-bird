package com.bird.cas.server.demo.handler;

import com.bird.cas.core.authentication.AbstractAuthenticationService;
import com.bird.cas.core.authentication.model.Credentials;
import com.bird.cas.core.authentication.model.Principal;
import com.bird.cas.core.authentication.model.SimplePrincipal;
import com.bird.cas.core.authentication.model.UsernamePasswordCredentials;
import com.bird.cas.core.exception.CasException;
import com.bird.cas.server.demo.entity.User;
import com.bird.cas.server.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: cas-bird
 * @description: 验证用户名密码
 * @author: JuFeng(ZhaoJun)
 * @create: 2021-04-09 10:05
 **/
@Component
public class UserAuthenticationHandler extends AbstractAuthenticationService {


    @Autowired
    private UserService userService;

    @Override
    public void preAuthenticate(HttpServletRequest request, HttpServletResponse response) {

    }

    @Override
    public void afterAuthenticate(HttpServletRequest request, HttpServletResponse response) {

    }

    @Override
    public void afterException(HttpServletRequest request, CasException exception) {

    }

    @Override
    public void afterAuthenticateSuccess(HttpServletRequest request, HttpServletResponse response) {

    }

    @Override
    public void preLogout(HttpServletRequest request, HttpServletResponse response) {

    }

    @Override
    public void afterLogout(HttpServletRequest request, HttpServletResponse response) {

    }

    /**
     * 必须实现该方法，来实现自己的用户验证逻辑
     * @param credentials
     * @return
     */
    @Override
    public Principal authenticate(Credentials credentials) {
        //这里如果你实现了Credentials 接口，请转换为你自己的实现类
        //如果没有实现，则使用默认的 UsernamePasswordCredentials.java
        UsernamePasswordCredentials usernamePasswordCredentials = (UsernamePasswordCredentials)credentials;
        String username = usernamePasswordCredentials.getUsername();
        String pwd = usernamePasswordCredentials.getPassword();
        User user = userService.getUserByUsername(username);
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(pwd)){
            throw  new CasException("请输入用户名和密码");
        }
        if ( user==null){
            throw  new CasException("用户不存在");
        }
        if (!user.getPassword().equals(pwd)){
            throw  new CasException("用户名和密码匹配");
        }
        // 这里是我使用了默认的Principal ，如果需要扩展可以实现 Principal 接口
        Map<String,Object> userProperty = new HashMap<>();
        userProperty.put("nickname",user.getNickname());
        userProperty.put("age",user.getAge());
        userProperty.put("tel",user.getTel());
        SimplePrincipal principal = new SimplePrincipal(user.getId(),userProperty);
        principal.setPass(true);
        return principal;
    }
}
