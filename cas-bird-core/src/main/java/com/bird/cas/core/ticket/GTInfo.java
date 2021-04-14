package com.bird.cas.core.ticket;

import com.bird.cas.core.authentication.model.Principal;
import lombok.Data;

import java.util.Map;

/**
 * @program: cas-bird
 * @description:
 * @author: JuFeng(ZhaoJun)
 * @create: 2021-04-08 17:45
 **/
@Data
public class GTInfo {
    private String gt;
    // 认证通过的用户信息
    private String id;
    Map<String, Object> attributes;
}
