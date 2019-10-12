package com.wudi.rabbit.messagereturnlistener;

import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * @author Dillon Wu
 * @Title: Customer
 * @Description: message confirm
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
        String queueNameError="testError";
        boolean durable = true;
        boolean exclusive = false;
        boolean autoDelete = false;
        connectionChannel.queueDeclare(queueName,durable,exclusive,autoDelete,null);

        //5:创建消费者,springboot从1.5.9升级到2.0.0，queueingconsumer报错没有这个类，改为使用 DefaultConsumer
        //TODO 每次从队列获取的数量
        //connectionChannel.basicQos(1);

        //6:设置Channel,1:队列名，2:是否自动签收（autoACK默认为true），3：消费者对象(callBack)
        Consumer consumer = new DefaultConsumer(connectionChannel) {
            //Envelope 主要存放生产者相关信息(比如:交换机，路由key)body是消息实体
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties, byte[] body)
                    throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println("Customer Received '" + message + "'");
                //TODO 处理完业务之后，手动删除消息
                //connectionChannel.basicAck(envelope.getDeliveryTag(),false);

            }
        };

        /**
         * 消费消息
         * queue 队列名称
         * autoAck true，消费端收到消息后，队列自动删除消息
         *         false，消费端收到消息后，需手动删除消息
         * callback 消费者对象
         */
        connectionChannel.basicConsume(queueName,true,consumer);
        //5:记得关闭相关连接
        //connectionChannel.close();
        //connection.close();
    }
}
