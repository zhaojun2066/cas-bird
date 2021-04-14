package com.bird.cas.core.authentication;

import com.bird.cas.core.authentication.AbstractAuthenticationService;
import com.bird.cas.core.authentication.model.Credentials;
import com.bird.cas.core.authentication.model.Principal;
import com.bird.cas.core.exception.CasException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @program: cas-bird
 * @description:
 * @author: JuFeng(ZhaoJun)
 * @create: 2021-04-08 14:35
 **/
public class AuthenticationService extends AbstractAuthenticationService {
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

    @Override
    public Principal authenticate(Credentials credentials) {
        return null;
    }
}
