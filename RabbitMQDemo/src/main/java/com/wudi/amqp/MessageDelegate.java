package com.wudi.amqp;

import lombok.Data;

/**
 * @author Dillon Wu
 * @Title: MessageDelegate
 * @Description: 适配器的实体
 * @date 2019/10/14 9:48
 */

public class MessageDelegate {

    public void handleMessage(byte[] messageBody){
        System.err.println("默认方法,消息内容:"+new String(messageBody));
        System.out.println("默认方法,消息内容:"+new String(messageBody));
    }

    public void consumeMessage(byte[] messageBody){
        System.out.println("字节数组方法,消息内容:"+new String(messageBody));
    }

    public void consumeMessage(String messageBody){
        System.out.println("字符串方法,消息内容:"+messageBody);
    }

    public void method1(String messageBody){
        System.out.println("====:"+messageBody);
    }

}
