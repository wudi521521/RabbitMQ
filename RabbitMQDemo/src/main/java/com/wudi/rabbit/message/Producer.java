package com.wudi.rabbit.message;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Dillon Wu
 * @Title: Producer
 * @Description: 测试 message 消息
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
        Map<String,Object> map = new HashMap<>();
        map.put("my1",11);
        map.put("my2",22);
        //设置属性
        AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder()
                .deliveryMode(2) //2是持久化
                .contentEncoding("UTF-8") //格式类型
                .expiration("10000") //10秒中过期,就是10s后没有被消费者消费就会过期。
                .headers(map) //自定义属性
                .build();

        //4:通过Channel发送数据,发送的数据需要转化Wie字节
        String msg = "Hello RabbitMQ";
        //路由键
        String routingKey="";
        String exchange="test_fanout_exchange";
        connectionChannel.basicPublish(exchange,routingKey,properties,msg.getBytes());


    }
}
