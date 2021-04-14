package com.bird.cas.core.query;

import lombok.Data;

/**
 * @program: cas-bird
 * @description:
 * @author: JuFeng(ZhaoJun)
 * @create: 2021-04-08 17:19
 **/
@Data
public class CheckStQuery {
    private String st;
    private String ip; // 接入系统的某台实例的ip
    private int port; // 接入系统的端口,ip port ,主要用于通知接入系统销毁自己的session

}
