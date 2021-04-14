package com.bird.cas.core.query;

import com.bird.cas.core.authentication.model.Credentials;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: cas-bird
 * @description:
 * @author: JuFeng(ZhaoJun)
 * @create: 2021-04-08 14:20
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginQuery {
    private String gt;
    private String service;
    private String eventId;
    private Credentials credentials;
}
