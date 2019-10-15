package com.wudi.config;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.MessageConverter;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.file.Files;
import java.util.UUID;

/**
 * @author Dillon Wu
 * @Title: ImageMessageConverter
 * @Description: image类型的转化
 * @date 2019/10/15 9:03
 */
public class ImageMessageConverter implements MessageConverter {

    /**
     * 将java对象转化为message
     * @param o
     * @param messageProperties
     * @return
     * @throws MessageConversionException
     */
    @Override
    public Message toMessage(Object o, MessageProperties messageProperties) throws MessageConversionException {
        throw new MessageConversionException(" convert error ! ");

    }

    /**
     * 将message转化为java对象
     * @param message
     * @return
     * @throws MessageConversionException
     */
    @Override
    public Object fromMessage(Message message) throws MessageConversionException {
        System.out.println("----------image messageConverter------------");
        Object _extName=message.getMessageProperties().getHeaders().get("extName");
        String extName=_extName == null ?"png":_extName.toString();

        byte[] body = message.getBody();
        String fileName = UUID.randomUUID().toString();
        String path="d:/"+fileName+"."+extName;
        File f = new File(path);
        try{
            Files.copy(new ByteArrayInputStream(body),f.toPath());

        }catch (Exception e){
            e.printStackTrace();
        }

        return f;
    }
}
