package com.bird.cas.core.ticket;

import com.bird.cas.core.authentication.model.Principal;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * @program: cas-bird
 * @description:
 * @author: JuFeng(ZhaoJun)
 * @create: 2021-04-08 16:58
 **/
@Data
public class STInfo implements Serializable {
    private String st;
    private String gt;// 关联的全局票
    private String id;
    Map<String, Object> attributes;
}
