package com.bird.cas.session;

import com.bird.cas.common.utils.CommonUtils;
import com.bird.cas.common.utils.URLUtil;
import com.bird.cas.session.store.SessionStoreFactory;
import com.bird.cas.session.store.StoreType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.net.MalformedURLException;

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
        if (CommonUtils.isNullString(sessionId)) {
            return new DistributionHttpSession(SessionStoreFactory.getSessionStore(StoreType.REDIS),request.getServletContext());
        }else {
            HttpSession httpSession = new DistributionHttpSession(sessionId, SessionStoreFactory.getSessionStore(StoreType.REDIS),request.getServletContext());
            this.sessionId = httpSession.getId();
        }
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
