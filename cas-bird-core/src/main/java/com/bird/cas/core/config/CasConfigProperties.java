package com.bird.cas.core.config;

import lombok.Data;
import me.zhyd.oauth.request.AuthRequest;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @program: ce-sso
 * @description:
 * @author: JuFeng(ZhaoJun)
 * @create: 2020-04-03 15:31
 **/
@Data
@ConfigurationProperties(prefix="bird.cas")
public class CasConfigProperties {

    private long gtExpireTime = 10080; // 60*24*7 全局票据超时时间
    private long stExpireTime = 300; // 临时票据给service的超时时间
    private long correlationSTAndGTExpireTime = 300;// GT ST 关联关系超时时间
    private long gtClientExpireTime = 10090 ;// gt 关联的client sessionId 超时时间，要比gt 失效时间要长 10s ,
   // private long sessionIdAndGTExpireTime = 10080;// sessionId 关联gt 的失效时间，要和gt 失效时间一样，目前没有用

    private boolean passwordEncode = false;
    private boolean cookieSecure = false;

    private List<String> whiteDomains;

}
