package com.ningmeng.producer;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * Created by 1 on 2020/2/14.
 */
public class Producer03_routing {
    //email队列   发送邮件
    private static final String QUEUE_INFORM_EMAIL = "queue_inform_email";
    //sms队列  发送短信
    private static final String QUEUE_INFORM_SMS = "queue_inform_sms";
    // ROUTING 类型交换机  有routingkey
    private static final String EXCHANGE_ROUTING_INFORM = "exchange_routing_inform";

    //psvm  快捷键
    public static void main(String[] args) {

        try {
            //创建连接工厂
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("127.0.0.1");
            factory.setPort(5672);//浏览器页面  15672 后台5672
            factory.setUsername("guest");
            factory.setPassword("guest");
            factory.setVirtualHost("/");//rabbimq默认虚拟机名称为 / ,虚拟机相当于一个独立的mq服务

            //创建于rabbitmq服务的TCP连接
            Connection connection = factory.newConnection();
            //创建信道  每个连接可以创建多个通道  每个通道代表一个会话任务
            Channel channel = connection.createChannel();
            /**
             * 声明交换机
             * String exchange,  交换机名称
             * BuiltinExchangeType type   交换机类型  fanout、topic、direct、headers
             */
            channel.exchangeDeclare(EXCHANGE_ROUTING_INFORM, BuiltinExchangeType.DIRECT);

            /**
             * 声明队列
             * String queue  队列名称
             * boolean durable   是否持久化  如果rabbitmq重启  消息会不会丢失
             * boolean exclusive  (互斥)队列是否独占此连接
             * boolean autoDelete  队列不再使用时  是否自动删除此队列
             *Map<String, Object> arguments  队列参数,设置队列存活时间等等
             * 如果 exclusive 和 autoDelete都为 true 就是一个临时队列
             */
            channel.queueDeclare(QUEUE_INFORM_EMAIL,true,false,false,null);
            channel.queueDeclare(QUEUE_INFORM_SMS,true,false,false,null);

            /**
             * 交换机和队列绑定
             * String queue,  队列名称
             * String exchange,   交换机名称
             * String routingKey   路由key  只有路由状态有routingkey
             */
            channel.queueBind(QUEUE_INFORM_EMAIL,EXCHANGE_ROUTING_INFORM,QUEUE_INFORM_EMAIL);
            channel.queueBind(QUEUE_INFORM_SMS,EXCHANGE_ROUTING_INFORM,QUEUE_INFORM_SMS);

            for(int i=0;i<5;i++){
                String message = "小明你好  你的邮件";
                /**
                 * 消息发布方法
                 * String exchange,  交换机名称   如果是普通队列，交换机为""
                 * String routingKey,  消息路由key   如果没有先用队列名代替
                 * BasicProperties props,  消息包含属性  工作中也用很少
                 * byte[] body  消息主体
                 */
                channel.basicPublish(EXCHANGE_ROUTING_INFORM,QUEUE_INFORM_EMAIL,null, message.getBytes());
                System.out.println("Send Message is:"+message);
            }

            for(int i=0;i<10;i++){
                String message = "小明你好  你的短信";
                /**
                 * 消息发布方法
                 * String exchange,  交换机名称   如果是普通队列，交换机为""
                 * String routingKey,  消息路由key   如果没有先用队列名代替
                 * BasicProperties props,  消息包含属性  工作中也用很少
                 * byte[] body  消息主体
                 */
                channel.basicPublish(EXCHANGE_ROUTING_INFORM,QUEUE_INFORM_SMS,null, message.getBytes());
                System.out.println("Send Message is:"+message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
