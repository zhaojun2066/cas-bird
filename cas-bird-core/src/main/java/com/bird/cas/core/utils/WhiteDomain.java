package com.bird.cas.core.utils;

import com.bird.cas.core.config.CasConfigProperties;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: cas-bird
 * @description:
 * @author: JuFeng(ZhaoJun)
 * @create: 2021-04-13 10:12
 **/
@Component
public class WhiteDomain {

    private static Map<String,String> whiteMap = new HashMap<>();

    private final CasConfigProperties casConfigProperties;

    public WhiteDomain(CasConfigProperties casConfigProperties) {
        this.casConfigProperties = casConfigProperties;
        List<String> domains = this.casConfigProperties.getWhiteDomains();
        if (!ObjectUtils.isEmpty(domains)){
            domains.forEach(x->{
                whiteMap.put(x,x);
            });
        }
    }

    public static boolean white(String domain){
        return whiteMap.containsKey(domain);
    }
}
