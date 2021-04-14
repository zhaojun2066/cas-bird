package com.bird.cas.core.authentication;

import com.bird.cas.core.authentication.model.Credentials;
import com.bird.cas.core.authentication.model.Principal;

/**
 * @program: cas-bird
 * @description: 验证用户
 * @author: JuFeng(ZhaoJun)
 * @create: 2021-04-07 13:58
 **/
public interface Authentication {
    /**
     * 进行认证,成功返回用户信息
     * @param credentials
     * @return
     */
    Principal authenticate(final Credentials credentials);
}
