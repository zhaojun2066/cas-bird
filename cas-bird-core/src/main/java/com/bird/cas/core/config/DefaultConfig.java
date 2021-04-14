package com.bird.cas.core.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @program: ce-sso
 * @description:
 * @author: JuFeng(ZhaoJun)
 * @create: 2020-04-03 22:11
 **/
@Configuration
@ComponentScan("com.bird.cas.core")
@EnableConfigurationProperties(CasConfigProperties.class)
public class DefaultConfig {




}
