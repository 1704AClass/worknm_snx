package com.ningmeng.test.mq;

import com.ningmeng.test.config.RabbitmqConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Created by 1 on 2020/2/14.
 */
@Component
public class ReceiveHandler {
    @RabbitListener(queues={RabbitmqConfig.QUEUE_INFORM_EMAIL})
    public void testEmailMQ(String msg){
        System.out.println("email:"+msg);
    }

    @RabbitListener(queues={RabbitmqConfig.QUEUE_INFORM_SMS})
    public void testSmsMQ(String msg){
        System.out.println("sms:"+msg);
    }
}
