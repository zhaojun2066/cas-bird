package com.bird.cas.client.dto;

import lombok.Data;

/**
 * @program: cas-bird
 * @description:
 * @author: JuFeng(ZhaoJun)
 * @create: 2020-04-09 14:46
 **/

@Data
public class ClientResponse {
    private int status = 111;
    private String location;
    private String result;
}
