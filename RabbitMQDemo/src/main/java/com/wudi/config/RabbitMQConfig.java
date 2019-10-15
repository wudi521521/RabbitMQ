package com.wudi.config;


import com.rabbitmq.client.Channel;
import com.sun.javafx.css.converters.PaintConverter;
import com.wudi.amqp.MessageDelegate;
import com.wudi.entity.Order;
import com.wudi.entity.Packaged;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.ConsumerTagStrategy;
import org.springframework.amqp.support.converter.ContentTypeDelegatingMessageConverter;
import org.springframework.amqp.support.converter.DefaultJackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.BufferedImageHttpMessageConverter;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author Dillon Wu
 * @Title: RabbitMQConfig
 * @Description: Rabbit的配置类, 当项目开启的时候注入到容器中
 * @date 2019/10/12 23:16
 */
@Configuration
@ComponentScan({"com.wudi.**"})
public class RabbitMQConfig {

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setAddresses("39.107.245.189:5672");
        connectionFactory.setUsername("eblocks_dev");
        connectionFactory.setPassword("eblocks2018@china.com");
        connectionFactory.setVirtualHost("/");

        return connectionFactory;
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
        rabbitAdmin.setAutoStartup(true);

        return rabbitAdmin;
    }

    /**
     * 按照这样可以添加多个，当项目开启的时候就会注入到容器里面
     *
     * @return
     */

    @Bean
    public TopicExchange exchange001() {

        return new TopicExchange("topic001", true, false); //持久交换机
    }

    @Bean
    public Queue queue001() {

        return new Queue("queue001", true); //持久队列
    }

    @Bean
    public Binding binding001() {

        return BindingBuilder.bind(queue001()).to(exchange001()).with("spring.#");
    }

    /**
     * 定义RabbitTemplate模板
     *
     * @param connectionFactory
     * @return
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {

        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);

        return rabbitTemplate;
    }

    /**
     * 定义SimpleMessageListenerContainer
     *
     * @param connectionFactory
     * @return
     */
    @Bean
    public SimpleMessageListenerContainer simpleMessageListenerContainer(ConnectionFactory connectionFactory) {

        SimpleMessageListenerContainer simpleMessageListenerContainer = new SimpleMessageListenerContainer(connectionFactory);

        //监控多个队列,只有这里添加了监控队列才会进行converter
        simpleMessageListenerContainer.setQueues(queue001());
        //当前消费者的数据
        simpleMessageListenerContainer.setConcurrentConsumers(1);
        //最大消费者的数量
        simpleMessageListenerContainer.setMaxConcurrentConsumers(5);
        simpleMessageListenerContainer.setDefaultRequeueRejected(false);
        //签收模式,自动签收
        simpleMessageListenerContainer.setAcknowledgeMode(AcknowledgeMode.AUTO);
        //自定义标签
        simpleMessageListenerContainer.setConsumerTagStrategy(new ConsumerTagStrategy() {
            @Override
            public String createConsumerTag(String s) {
                return s + "_" + UUID.randomUUID().toString();
            }
        });
        //监听
     /* simpleMessageListenerContainer.setMessageListener(new ChannelAwareMessageListener() {
            @Override
            public void onMessage(Message message, Channel channel) throws Exception {
                 //获取数据
                String data = new String(message.getBody());
                System.out.println("数据打印===监听:"+data);
            }
        });*/
        //TODO 1:适配器的第一种方法

       /* //使用适配器
        MessageListenerAdapter messageListenerAdapter = new MessageListenerAdapter(new MessageDelegate());
        //指定默认的处理方法
        messageListenerAdapter.setDefaultListenerMethod("consumeMessage");
        //放入适配器
        simpleMessageListenerContainer.setMessageListener(messageListenerAdapter);
        //添加一个转化器:从字节数组转换为String
        simpleMessageListenerContainer.setMessageConverter(new TextMessageConverter());*/

        //TODO 1.1支持json格式的转换器
        /*//使用适配器
        MessageListenerAdapter messageListenerAdapter = new MessageListenerAdapter(new MessageDelegate());
        //指定默认的处理方法
        messageListenerAdapter.setDefaultListenerMethod("consumeMessage");
        Jackson2JsonMessageConverter messageConverter = new Jackson2JsonMessageConverter();
        messageListenerAdapter.setMessageConverter(messageConverter);
        //放入适配器
        simpleMessageListenerContainer.setMessageListener(messageListenerAdapter);*/

        //TODO 1.2支持DefaultJackson2JavaTypeMapper & Jackson2JsonMessageConverter 支持java对象转换
       /* //使用适配器
        MessageListenerAdapter messageListenerAdapter = new MessageListenerAdapter(new MessageDelegate());
        //指定默认的处理方法
        messageListenerAdapter.setDefaultListenerMethod("consumeMessage");
        Jackson2JsonMessageConverter messageConverter = new Jackson2JsonMessageConverter();

        DefaultJackson2JavaTypeMapper defaultJackson2JavaTypeMapper = new DefaultJackson2JavaTypeMapper();
        messageConverter.setJavaTypeMapper(defaultJackson2JavaTypeMapper);

        messageListenerAdapter.setMessageConverter(messageConverter);

        //放入适配器
        simpleMessageListenerContainer.setMessageListener(messageListenerAdapter);*/

        //TODO 1.3支持DefaultJackson2JavaTypeMapper & Jackson2JsonMessageConverter 支持java对象多映射转换
       /* //使用适配器
        MessageListenerAdapter messageListenerAdapter = new MessageListenerAdapter(new MessageDelegate());
        //指定默认的处理方法
        messageListenerAdapter.setDefaultListenerMethod("consumeMessage");
        Jackson2JsonMessageConverter messageConverter = new Jackson2JsonMessageConverter();

        DefaultJackson2JavaTypeMapper defaultJackson2JavaTypeMapper = new DefaultJackson2JavaTypeMapper();
        Map<String,Class<?>> idClassMapping =new HashMap<String,Class<?>>();
        idClassMapping.put("order", Order.class);
        idClassMapping.put("packaged", Packaged.class);
        defaultJackson2JavaTypeMapper.setIdClassMapping(idClassMapping);
        messageConverter.setJavaTypeMapper(defaultJackson2JavaTypeMapper);

        messageListenerAdapter.setMessageConverter(messageConverter);

        //放入适配器
        simpleMessageListenerContainer.setMessageListener(messageListenerAdapter);
*/
        //TODO 1.4 ext convert
        MessageListenerAdapter messageListenerAdapter = new MessageListenerAdapter(new MessageDelegate());
        messageListenerAdapter.setDefaultListenerMethod("consumeMessage");
        ContentTypeDelegatingMessageConverter convert = new ContentTypeDelegatingMessageConverter();

        //全局的转换器
        TextMessageConverter textConvert = new TextMessageConverter();
        convert.addDelegate("text", textConvert);
        convert.addDelegate("html/text", textConvert);
        convert.addDelegate("xml/text", textConvert);
        convert.addDelegate("text/plain", textConvert);

        //json格式转化成java
        Jackson2JsonMessageConverter jackson2JsonMessageConverter = new Jackson2JsonMessageConverter();
        convert.addDelegate("json",jackson2JsonMessageConverter);
        convert.addDelegate("application/json",jackson2JsonMessageConverter);

        //图片转化成java
        ImageMessageConverter imageMessageConverter = new ImageMessageConverter();
        convert.addDelegate("image/png",imageMessageConverter);
        convert.addDelegate("image",imageMessageConverter);

        //PDF转化成java
        PDFMessageConverter pdfMessageConverter = new PDFMessageConverter();
         convert.addDelegate("application/pdf",pdfMessageConverter);

        messageListenerAdapter.setMessageConverter(convert);
        //放入适配器
        simpleMessageListenerContainer.setMessageListener(messageListenerAdapter);
          //TODO 适配器方式:我们的队列名称和方法名称也可以进行一一的匹配
        /*MessageListenerAdapter messageListenerAdapter = new MessageListenerAdapter(new MessageDelegate());
        Map<String,String> queueOrTagMethodName = new HashMap<>();
        queueOrTagMethodName.put("exchange","routingKey");

        messageListenerAdapter.setQueueOrTagToMethodName(queueOrTagMethodName);
        simpleMessageListenerContainer.setMessageListener(messageListenerAdapter);*/
        return simpleMessageListenerContainer;
    }

}
