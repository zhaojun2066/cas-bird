package com.bird.cas.core.ticket;

import lombok.Data;

import java.io.Serializable;

/**
 * @program: cas-bird
 * @description:
 * @author: JuFeng(ZhaoJun)
 * @create: 2021-04-08 17:07
 **/
@Data
public class SystemClient implements Serializable {
    private String ip;
    private int port;
    private String st;
}
