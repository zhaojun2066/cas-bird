package com.bird.cas.core.listener;

import com.bird.cas.core.ticket.TicketManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import static com.bird.cas.common.PubConstant.PREFIX_GT_KEY;

/**
 * @program: cas-bird
 * @description: GT 失效 监听
 * @author: JuFeng(ZhaoJun)
 * @create: 2020-04-23 14:53
 **/
@Slf4j
@Component
public class GTKeyExpiredEventMessageListener implements MessageListener {

    @Autowired
    private TicketManager ticketManager;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String expiredMsg = message.toString();
        log.info("message=> {}",message);
        // 判断是否为GT 的失效
        if (expiredMsg.startsWith(PREFIX_GT_KEY)){
            String gt = expiredMsg.replace(PREFIX_GT_KEY,"");
            ticketManager.logoutGT(gt); // 注销gt
            if (log.isDebugEnabled()){
                log.debug(" expire gt=> {}" ,gt);
            }

        }
    }
}
