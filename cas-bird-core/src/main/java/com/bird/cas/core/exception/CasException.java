package com.bird.cas.core.exception;

import com.bird.cloud.common.exception.BusinessException;

/**
 * @program: cas-bird
 * @description:
 * @author: JuFeng(ZhaoJun)
 * @create: 2021-04-07 15:22
 **/
public class CasException extends BusinessException {
    public CasException(String message) {
        super(message);
    }
}
