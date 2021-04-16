package com.bird.cas.session;

import com.bird.cas.common.utils.CommonUtils;
import com.bird.cas.common.utils.CookieUtil;
import com.bird.cas.common.utils.URLUtil;
import com.bird.cas.session.factory.Factory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.net.MalformedURLException;

import static com.bird.cas.common.PubConstant.SESSION_ID_COOKIE_KEY;

/**
 * @program: cas-bird
 * @description:
 * @author: JuFeng(ZhaoJun)
 * @create: 2021-04-14 17:09
 **/
public class DistributionSessionRequestWrapper extends HttpServletRequestWrapper {

    private HttpServletRequest request;

    private HttpServletResponse response;

    private String sessionId;

    /**
     * Constructs a request object wrapping the given request.
     *
     * @param request
     * @throws IllegalArgumentException if the request is null
     */
    public DistributionSessionRequestWrapper(HttpServletRequest request,HttpServletResponse response) {
        super(request);
        this.request = request;
        this.response = response;
    }

    @Override
    public HttpSession getSession(boolean create) {
        HttpSession httpSession = null;
        if (!CommonUtils.isNullString(sessionId)) {
            httpSession = Factory.createExitHttpSession(sessionId,request.getServletContext());
        } else if (create){
            Factory.createNewHttpSession(request.getServletContext());
        } else {
           return null;
        }
        this.sessionId = httpSession.getId();
        CookieUtil.set(response,getFirstLevelDomain(), SESSION_ID_COOKIE_KEY,sessionId,"/",false);
        return null;
    }

    @Override
    public HttpSession getSession() {
        return this.getSession(true);
    }

    @Override
    public String getRequestedSessionId() {
        return this.sessionId;
    }

    private String getFirstLevelDomain(){
        try {
            return URLUtil.getDomainName(this.request.getRequestURL().toString());
        } catch (MalformedURLException e) {
        }

        return null;
    }
}
