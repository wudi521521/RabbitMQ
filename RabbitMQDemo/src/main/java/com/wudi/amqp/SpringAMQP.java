package com.wudi.amqp;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

/**
 * @author Dillon Wu
 * @Title: SpringAMQP
 * @Description: RabbitMQ整合Spring AMQP
 * @date 2019/10/12 23:31
 */
@RestController
@RequestMapping("/test/spring")
public class SpringAMQP {

    @Autowired
    private RabbitAdmin rabbitAdmin;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 测试RabbitAdmin
     */
    @RequestMapping("/amqp")
    public void testSpringAMQP() {
        //1:声明交换器
        rabbitAdmin.declareExchange(new TopicExchange("test.topic.exchange", false, false));
        rabbitAdmin.declareExchange(new DirectExchange("test.direct.exchange", false, false));
        rabbitAdmin.declareExchange(new HeadersExchange("test.header.exchange", false, false));
        rabbitAdmin.declareExchange(new FanoutExchange("test.fanout.exchange", false, false));

        //2:声明队列
        rabbitAdmin.declareQueue(new Queue("test.topic.queue", false));
        rabbitAdmin.declareQueue(new Queue("test.direct.queue", false));
        rabbitAdmin.declareQueue(new Queue("test.header.queue", false));
        rabbitAdmin.declareQueue(new Queue("test.fanout.queue", false));

        //3:绑定队列
        rabbitAdmin.declareBinding(new Binding("test.topic.queue",
                Binding.DestinationType.QUEUE,
                "test.topic.exchange",
                "test#",
                new HashMap<>()));
        rabbitAdmin.declareBinding(new Binding("test.direct.queue",
                Binding.DestinationType.QUEUE,
                "test.direct.exchange",
                "direct",
                new HashMap<>()));
        rabbitAdmin.declareBinding(new Binding("test.fanout.queue",
                Binding.DestinationType.QUEUE, "" +
                "test.fanout.exchange",
                "fanout",
                new HashMap<>()));
        rabbitAdmin.declareBinding(new Binding("test.header.queue",
                Binding.DestinationType.QUEUE, "" +
                "test.header.exchange",
                "header",
                new HashMap<>()));

        //4:直接声明绑定交换器与路由
        //a:topic 主题交换器
        rabbitAdmin.declareBinding(BindingBuilder.
                bind(new Queue("test.topic.queue", false)).
                to(new TopicExchange("test.topic.exchange", false, false)).
                with("user.#"));
        //b:fanout 扇形交换器(不走路由键)
        rabbitAdmin.declareBinding(BindingBuilder.
                bind(new Queue("test.fanout.queue", false)).
                to(new FanoutExchange("test.fanout.exchange", false, false)));
        //c:direct 直连交换器
        rabbitAdmin.declareBinding(BindingBuilder.
                bind(new Queue("test.direct.queue", false)).
                to(new DirectExchange("test.direct.exchange", false, false)).
                with("user"));

        //5:清空队列数据
        rabbitAdmin.purgeQueue("test.topic.queue", false);
    }

    /**
     * 测试template
     */
    @RequestMapping("template1")
    public void rabbitTemplateTest() throws Exception {
        //1:创建信息
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.getHeaders().put("desc", "信息描述");
        messageProperties.getHeaders().put("type", "自定义类型");
        Message message = new Message("Hello RabbiMQ".getBytes(), messageProperties);

        //2:发送信息
        rabbitTemplate.convertAndSend("test.topic.exchange", "test#", message, new MessagePostProcessor() {
            //发送完消息在进行增加或者修改消息
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                System.out.println("---------------添加额外设置");
                message.getMessageProperties().getHeaders().put("type", "增加新的修改");
                message.getMessageProperties().getHeaders().put("attr", "增加新的修改--------");

                return message;
            }
        });

    }

    /**
     * Demo测试二
     */
    @RequestMapping("template2")
    public void rabbitTemplateTest2(){
        //1:创建信息
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setContentType("text/plain");
        messageProperties.getHeaders().put("desc", "信息描述");
        messageProperties.getHeaders().put("type", "自定义类型");
        Message message = new Message("Hello RabbiMQ".getBytes(), messageProperties);

        //2:发送信息
        rabbitTemplate.convertAndSend("topic001","spring.#",message);

    }
}
