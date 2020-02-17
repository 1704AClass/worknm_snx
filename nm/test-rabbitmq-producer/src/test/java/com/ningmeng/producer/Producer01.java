package com.ningmeng.producer;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.util.Date;

/**
 * 入门程序 生产者
 * Created by 1 on 2020/2/13.
 */
public class Producer01 {
    //队列名称
    private static final String QUEUE = "helloworld";
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
             * 声明队列
             * String queue  队列名称
             * boolean durable   是否持久化  如果rabbitmq重启  消息会不会丢失
             * boolean exclusive  (互斥)队列是否独占此连接
             * boolean autoDelete  队列不再使用时  是否自动删除此队列
             *Map<String, Object> arguments  队列参数,设置队列存活时间等等
             * 如果 exclusive 和 autoDelete都为 true 就是一个临时队列
             */
            channel.queueDeclare(QUEUE,true,false,false,null);
            String message = "小明你好";
            System.out.println("send:"+message+","+"时间:"+ new Date());
            /**
             * 消息发布方法
             * String exchange,  交换机名称   如果是普通队列，交换机为""
             * String routingKey,  消息路由key   如果没有先用队列名代替
             * BasicProperties props,  消息包含属性  工作中也用很少
             * byte[] body  消息主体
             */
            channel.basicPublish("",QUEUE,null, message.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
