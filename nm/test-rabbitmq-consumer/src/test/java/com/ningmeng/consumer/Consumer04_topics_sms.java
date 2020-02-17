package com.ningmeng.consumer;

import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * Created by 1 on 2020/2/14.
 */
public class Consumer04_topics_sms {
    //sms队列  发送短信
    private static final String QUEUE_INFORM_SMS = "queue_inform_sms";
    // topics 类型交换机
    private static final String EXCHANGE_TOPICS_INFORM = "exchange_topics_inform";

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
            channel.exchangeDeclare(EXCHANGE_TOPICS_INFORM, BuiltinExchangeType.TOPIC);

            /**
             * 声明队列
             * String queue  队列名称
             * boolean durable   是否持久化  如果rabbitmq重启  消息会不会丢失
             * boolean exclusive  (互斥)队列是否独占此连接
             * boolean autoDelete  队列不再使用时  是否自动删除此队列
             *Map<String, Object> arguments  队列参数,设置队列存活时间等等
             * 如果 exclusive 和 autoDelete都为 true 就是一个临时队列
             */
            channel.queueDeclare(QUEUE_INFORM_SMS,true,false,false,null);

            /**
             * 交换机和队列绑定
             * String queue,  队列名称
             * String exchange,   交换机名称
             * String routingKey   路由key  只有路由状态有routingkey
             */
            channel.queueBind(QUEUE_INFORM_SMS,EXCHANGE_TOPICS_INFORM,"inform.#.sms.#");

            //消费消息方法
            Consumer consumer = new DefaultConsumer(channel){
                /**
                 * 消费者接收消息调用此方法
                 * @param consumerTag  消费者的标签  在channel.basicConsue()去指定
                 * @param envelope  消息包的内容  可从中获取消息id  消息routingkey  交换机
                 * @param properties
                 * @param body
                 * @throws IOException
                 */
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope,
                                           AMQP.BasicProperties properties, byte[] body) throws IOException {
                    //获取交换机
                    String exchange = envelope.getExchange();
                    //路由key
                    String routingKey = envelope.getRoutingKey();
                    //消息id
                    long deliveryTag = envelope.getDeliveryTag();
                    //消息内容
                    String str = new String(body, "utf-8");
                    System.out.println("receive message.."+ str);
                }
            };
            /**
             * 监听队列
             * String queue,队列名称
             * boolean autoAck, 是否自动回复  设置true表示接受到自动向mq回复接收到了
             * Consumer callback  消息消费方法  消费者接收到消息调用此方法
             */
            channel.basicConsume(QUEUE_INFORM_SMS,true,consumer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
