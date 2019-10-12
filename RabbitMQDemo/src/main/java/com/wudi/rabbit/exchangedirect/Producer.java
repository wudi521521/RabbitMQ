package com.wudi.rabbit.exchangedirect;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * @author Dillon Wu
 * @Title: Producer
 * @Description: 生产者 direct exchange(直接交换器)
 * @date 2019/10/11 8:52
 */
public class Producer {

    public static void main(String[] args) throws Exception {
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

        //4:通过Channel发送数据,发送的数据需要转化Wie字节
        String msg = "Hello RabbitMQ";
        String routingKey="test.direct";
        String exchange="test_direct_exchange";
        connectionChannel.basicPublish(exchange,routingKey,null,msg.getBytes());

        //5:记得关闭相关连接
      /*  connectionChannel.close();
        connection.close();*/
    }
}
