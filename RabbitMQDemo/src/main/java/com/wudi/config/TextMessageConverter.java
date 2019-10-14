package com.wudi.config;


import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.MessageConverter;


/**
 * @author Dillon Wu
 * @Title: TextMessageConverter
 * @Description: TODO
 * @date 2019/10/14 10:33
 */
public class TextMessageConverter implements MessageConverter {

    /**
     * java对象转化为message对象
     * @param o
     * @param messageProperties
     * @return
     * @throws MessageConversionException
     */
    @Override
    public Message toMessage(Object o, MessageProperties messageProperties) throws MessageConversionException {
        System.out.println("===========TextMessageConverter ==========");

        return new Message(o.toString().getBytes(),messageProperties);
    }

    /**
     * message 对象转化为 java对象
     * @param message
     * @return
     * @throws MessageConversionException
     */
    @Override
    public Object fromMessage(Message message) throws MessageConversionException {
        System.out.println("======TextMessageConverter===========");
        String contentType = message.getMessageProperties().getContentType();
        //含有text才可以直接转换成字符串
        if (null !=contentType && contentType.contains("text")){
            return new String (contentType);
        }
        return message.getBody();
    }
}
