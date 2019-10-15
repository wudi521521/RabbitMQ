package com.wudi.amqp;
import com.wudi.entity.Order;
import com.wudi.entity.Packaged;
import lombok.Data;

import java.io.File;
import java.util.Map;

/**
 * @author Dillon Wu
 * @Title: MessageDelegate
 * @Description: 适配器的实体
 * @date 2019/10/14 9:48
 */

public class MessageDelegate {

    public void handleMessage(byte[] messageBody) {
        System.err.println("默认方法,消息内容:" + new String(messageBody));
        System.out.println("默认方法,消息内容:" + new String(messageBody));
    }

    public void consumeMessage(byte[] messageBody) {
        System.out.println("字节数组方法,消息内容:" + new String(messageBody));
    }

    public void consumeMessage(String messageBody) {
        System.out.println("字符串方法,消息内容:" + messageBody);
    }

    public void consumeMessage(Map messageBody) {
        System.out.println("map方法 消息内容:" + messageBody);
    }

    public void method1(String messageBody) {
        System.out.println("====:" + messageBody);
    }

    public void consumeMessage(Order order) {
        System.err.println("order 对象,消息内容,id:" + order.getId() + ",name:" + order.getName() + ",content:" + order.getContent());
    }

    public void consumeMessage(Packaged packaged) {
        System.out.println("package 对象,消息内容,id:" + packaged.getId() + ",name:" + packaged.getName() + ",describe:" + packaged.getDescription());
    }

    public void consumeMessage(File file) {
        System.out.println("文件对象方法，消息内容:" + file.getName());
    }

}
