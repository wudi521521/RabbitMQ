package com.wudi.amqp.messageconverter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wudi.entity.Order;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author Dillon Wu
 * @Title: TestSendJsonMessage
 * @Description: 测试发送数据转为json
 * @date 2019/10/14 13:47
 */
@RestController
@RequestMapping("/test/converter")
public class TestSendJsonMessage {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 监听处返回的是一个json
     * @throws Exception
     */
    @RequestMapping("json")
    public void testSendJsonMessage() throws Exception{
        Order order = new Order();
        order.setId("001");
        order.setName("发送Json格式");
        order.setContent("格式类型:application/json");

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(order);
        System.err.println("order json:"+json);
        MessageProperties messageProperties = new MessageProperties();

        //TODO 这里注意一定要修改contentType为application/json
        messageProperties.setContentType("application/json");
        Message message = new Message(json.getBytes(),messageProperties);

        rabbitTemplate.send("topic001","spring.#",message);

    }

    /**
     * 监听处返回的是order实体
     * @throws Exception
     */
    @RequestMapping("order")
    public void testSendOrderMessage() throws Exception{
        Order order = new Order();
        order.setId("001");
        order.setName("发送Json格式");
        order.setContent("格式类型:application/json");

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(order);
        System.err.println("order Entity json:"+json);
        MessageProperties messageProperties = new MessageProperties();

        //TODO 这里注意一定要修改contentType为application/json
        messageProperties.setContentType("application/json");

        //TODO __TypeId__ 固定写法(下滑先是连着的两个"__")
        messageProperties.getHeaders().put("__TypeId__","order");
        Message message = new Message(json.getBytes(),messageProperties);

        rabbitTemplate.send("topic001","spring.#",message);

    }

    //@RequestMapping("mun")
    public void testSendMunMessage() throws Exception{
        Order order = new Order();
        order.setId("001");
        order.setName("发送Json格式");
        order.setContent("格式类型:application/json");

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(order);
        System.err.println("order Entity json:"+json);
        MessageProperties messageProperties = new MessageProperties();

        //TODO 这里注意一定要修改contentType为application/json
        messageProperties.setContentType("application/json");

        //TODO __TypeId__ 固定写法(下滑先是连着的两个"__")
        messageProperties.getHeaders().put("__TypeId__","com.wudi.entity.Order");
        Message message = new Message(json.getBytes(),messageProperties);

        rabbitTemplate.send("topic001","spring.#",message);

    }
    @RequestMapping("text")
    public void testSendTextMessage() throws Exception{
        Order order = new Order();
        order.setId("001");
        order.setName("发送Json格式");

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(order);
        System.out.println("order json:"+json);
        MessageProperties messageProperties = new MessageProperties();

        //这里注意一定要修改contentType为application/json
        messageProperties.setContentType("text/content");

        Message message = new Message(json.getBytes(),messageProperties);
        rabbitTemplate.send("topic001","spring.order",message);
    }
    @RequestMapping("pdf")
    public void testSendPDFMessage() throws Exception{
        byte[] body = Files.readAllBytes(Paths.get("d:/11","aa.pdf"));
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setContentType("application/pdf");
        Message message = new Message(body,messageProperties);
        rabbitTemplate.send("topic001","spring.order",message);
    }

    @RequestMapping("image")
    public void testSendImageMessage() throws Exception{
        byte[] body = Files.readAllBytes(Paths.get("d:/11","aa.png"));
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setContentType("image/png");
        messageProperties.getHeaders().put("extName","png");
        Message message = new Message(body,messageProperties);
        rabbitTemplate.send("topic001","spring.order",message);
    }
}
