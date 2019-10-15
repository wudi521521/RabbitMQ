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
 * @Title: PDFMessageConverter
 * @Description: pdf类型的数据转化
 * @date 2019/10/15 9:03
 */
public class PDFMessageConverter implements MessageConverter {
    /**
     * java对象转化为message对象
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
     * message 对象转化为 java对象
     * @param message
     * @return
     * @throws MessageConversionException
     */
    @Override
    public Object fromMessage(Message message) throws MessageConversionException {
        System.err.println("============PDFMessage=================");
        byte[] body = message.getBody();
        String fileName = UUID.randomUUID().toString();
        String path ="d:/"+fileName+".pdf";
        File file = new File(path);
        try{
            Files.copy(new ByteArrayInputStream(body),file.toPath());

        }catch (Exception e){
            e.printStackTrace();
        }
        return file;
    }
}
