package com.wudi.rabbit.messagelimit;

import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * @author Dillon Wu
 * @Title: Customer
 * @Description: 消息限流
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

        //限流第一件事就是autoAck设置为false,int prefetchSize, int prefetchCount, boolean global
        int prefetchSize=0;//表示不限制
        int prefetchCount = 1;//一次推送消息的数量
        boolean global=true; //true/false是否将上面应用于channel还是consumer
        connectionChannel.basicQos(prefetchSize,prefetchCount,global);

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
                //long deliveryTag  消息的标签
                // boolean multiple 是否批量签收
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
        //5:记得关闭相关连接
        //connectionChannel.close();
        //connection.close();
    }
}
