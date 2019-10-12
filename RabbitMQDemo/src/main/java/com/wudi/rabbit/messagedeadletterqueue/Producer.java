 package com.wudi.rabbit.messagedeadletterqueue;

import com.rabbitmq.client.*;
import com.rabbitmq.client.impl.AMQBasicProperties;

import java.io.IOException;

 /**
  * @author Dillon Wu
  * @Title: Producer
  * @Description: test dead-letter-queue
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

         //4:指定我们的消息投递模式:消息的确认
         connectionChannel.confirmSelect();

         //a:设置过期的时间
         //设置属性
         AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder()
                 .deliveryMode(2) //2是持久化
                 .contentEncoding("UTF-8") //格式类型
                 .expiration("10000") //10秒中过期,就是10s后没有被消费者消费就会过期。
                 .build();

         //5:通过Channel发送数据,发送的数据需要转化Wie字节
         String msg = "Hello RabbitMQ";
         String routingKey="test001";
         String exchange="test_002";
         boolean mandatory=true;
         //waiting for 10 seconds

         connectionChannel.basicPublish(exchange,routingKey,mandatory,properties,msg.getBytes());

         //6:添加一个确认监听(是生产者----》Broker)
         connectionChannel.addConfirmListener(new ConfirmListener() {
             @Override
             public void handleAck(long deliveryTag, boolean multiple) throws IOException {
                 System.out.println("---------ack--------------");
             }

             @Override
             public void handleNack(long deliveryTag, boolean multiple) throws IOException {
                 System.out.println("---------no ack-------------------------");
             }
         });
         //7:添加返回监听(Broker --> 到消费者)
         connectionChannel.addReturnListener(new ReturnListener() {
             //replyCode, replyText, exchange, routingKey, properties, body
             //replyCode 响应码
             //replyText 响应的数据
             //exchange 交换器
             //routingKey 路由键
             //properties 设置属性
             @Override
             public void handleReturn(int replyCode, String replyText, String exchange, String routingKey, AMQP.BasicProperties basicProperties, byte[] bytes) throws IOException {
                 System.out.println("=======handle return========");
                 System.out.println("replyCode"+replyCode);
                 System.out.println("replyText:"+replyText);
                 System.out.println("Exchange:"+exchange);
             }
         });

         //5:记得关闭相关连接
         //connectionChannel.close();
         //connection.close();
     }
 }
