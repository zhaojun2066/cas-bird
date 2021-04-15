package com.bird.cas.core.listener;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisClusterConnection;
import org.springframework.data.redis.connection.RedisClusterNode;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.util.StringUtils;
import redis.clients.jedis.JedisShardInfo;

/**
 * @program: cas-bird
 * @description:
 * @author: JuFeng(ZhaoJun)
 * @create: 2020-04-23 14:51
 **/
public class RedisMessageListenerFactory implements BeanFactoryAware, ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private RedisProperties redisProperties;

    private DefaultListableBeanFactory beanFactory;

    private RedisConnectionFactory redisConnectionFactory;

    @Autowired
    private MessageListener messageListener;

    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = (DefaultListableBeanFactory) beanFactory;
    }

    public void setRedisConnectionFactory(RedisConnectionFactory redisConnectionFactory) {
        this.redisConnectionFactory = redisConnectionFactory;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        RedisClusterConnection redisClusterConnection = redisConnectionFactory.getClusterConnection();
        if (redisClusterConnection != null) {
            Iterable<RedisClusterNode> nodes = redisClusterConnection.clusterGetNodes();
            for (RedisClusterNode node : nodes) {
                if (node.isMaster()) {
                    String containerBeanName = "messageContainer" + node.hashCode();
                    if (beanFactory.containsBean(containerBeanName)) {
                        return;
                    }
                    JedisShardInfo jedisShardInfo = new JedisShardInfo(node.getHost(), node.getPort());
                    String password = redisProperties.getPassword();
                    if (!StringUtils.isEmpty(password)){
                        jedisShardInfo.setPassword(password);
                    }
                    JedisConnectionFactory factory = new JedisConnectionFactory(jedisShardInfo);
                    BeanDefinitionBuilder containerBeanDefinitionBuilder = BeanDefinitionBuilder
                            .genericBeanDefinition(RedisMessageListenerContainer.class);
                    containerBeanDefinitionBuilder.addPropertyValue("connectionFactory", factory);
                    containerBeanDefinitionBuilder.setScope(BeanDefinition.SCOPE_SINGLETON);
                    containerBeanDefinitionBuilder.setLazyInit(false);
                    beanFactory.registerBeanDefinition(containerBeanName,
                            containerBeanDefinitionBuilder.getRawBeanDefinition());

                    RedisMessageListenerContainer container = beanFactory.getBean(containerBeanName,
                            RedisMessageListenerContainer.class);
                    String listenerBeanName = "messageListener" + node.hashCode();
                    if (beanFactory.containsBean(listenerBeanName)) {
                        return;
                    }
                    container.addMessageListener(messageListener, new PatternTopic("__keyevent@0__:expired"));
                    container.start();
                }
            }
        }
    }
}
