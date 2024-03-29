package com.wudi.rabbit.messagedeadletterexchange;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Dillon Wu
 * @Title: Customer
 * @Description: test dead-letter-queue
 * @date 2019/10/11 8:51
 */
public class Customer {

    public static void main(String[] args) throws Exception{
        //1创建一个ConnectionFactory
        ConnectionFactory connectionFactory = new ConnectionFactory();
         connectionFactory.setHost("39.107.245.189");
         connectionFactory.setPort(5672);
         connectionFactory.setUsername("eblocks_dev");
         connectionFactory.setPassword("eblocks2018@china.com");
         connectionFactory.setVirtualHost("/");

        //2:通过连接工厂创建连接
        Connection connection = connectionFactory.newConnection();

        //3:通过connection创建一个Channel
        Channel connectionChannel = connection.createChannel();

        //4:声明一个队列,1:队列名，2:是否持久化(durable 默认为true)，3：是否独占（exclusive 默认为true） 4:是否自动删除(autoDelete)
        String queueName="test001";
        boolean durable = true;
        boolean exclusive = false;
        boolean autoDelete = false;
        Map<String,Object> argument=new HashMap<String,Object>();
        argument.put("x-dead-letter-exchange","dlx.exchange");

        //b:这个argument属性,要设置到声明队列上
        connectionChannel.exchangeDeclare("test_002","topic",true,false,null);
        connectionChannel.queueDeclare(queueName,true,exclusive,autoDelete,argument);
        connectionChannel.queueBind(queueName,"test_002","#");
        //a:进行死信队列声明
        connectionChannel.exchangeDeclare("dlx.exchange","topic",true,false,null);
        connectionChannel.queueDeclare("dlx.queue",true,exclusive,autoDelete,null);
        connectionChannel.queueBind("dlx.queue","dlx.exchange","#");



        //限流第一件事就是autoAck设置为false,int prefetchSize, int prefetchCount, boolean global

        //6:设置Channel,1:队列名，2:是否自动签收（autoACK默认为true），3：消费者对象(callBack)
        Consumer consumer = new DefaultConsumer(connectionChannel) {
            //Envelope 主要存放生产者相关信息(比如:交换机，路由key)body是消息实体
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties, byte[] body)
                    throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println("Customer Received '" + message + "'");

                    connectionChannel.basicAck(envelope.getDeliveryTag(),false);
            }
        };

        /**
         * 消费消息
         * queue 队列名称
         * autoAck true，消费端收到消息后，队列自动删除消息
         *         false，消费端收到消息后，需手动删除消息
         * callback 消费者对象
         */
        connectionChannel.basicConsume(queueName,false,consumer);

    }
}
