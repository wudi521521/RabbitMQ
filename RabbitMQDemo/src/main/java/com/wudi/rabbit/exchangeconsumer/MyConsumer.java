package com.wudi.rabbit.exchangeconsumer;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.IOException;

/**
 * @author Dillon Wu
 * @Title: MyConsumer
 * @Description: TODO
 * @date 2019/10/12 8:38
 */
public class MyConsumer extends DefaultConsumer {
    public MyConsumer(Channel channel) {
        super(channel);
    }

    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
        System.out.println("================MyConsumer===============");
        System.out.println("=======consumerTag=======:"+consumerTag);
        System.out.println("========envelope=================:"+envelope);
        System.out.println("=============body====================:"+body.toString());
    }
}
