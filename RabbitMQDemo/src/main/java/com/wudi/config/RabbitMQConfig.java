package com.wudi.config;


import com.rabbitmq.client.Channel;
import com.wudi.amqp.MessageDelegate;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.ConsumerTagStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author Dillon Wu
 * @Title: RabbitMQConfig
 * @Description: Rabbit的配置类,当项目开启的时候注入到容器中
 * @date 2019/10/12 23:16
 */
@Configuration
@ComponentScan({"com.wudi.**"})
public class RabbitMQConfig {

    @Bean
    public ConnectionFactory connectionFactory(){
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setAddresses("39.107.245.189:5672");
        connectionFactory.setUsername("eblocks_dev");
        connectionFactory.setPassword("eblocks2018@china.com");
        connectionFactory.setVirtualHost("/");

        return connectionFactory;
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory){
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
        rabbitAdmin.setAutoStartup(true);

        return rabbitAdmin;
    }

    /**
     * 按照这样可以添加多个，当项目开启的时候就会注入到容器里面
     * @return
     */

    @Bean
    public TopicExchange exchange001(){

        return new TopicExchange("topic001",true,false); //持久交换机
    }

    @Bean
    public Queue queue001(){

        return new Queue("queue001",true); //持久队列
    }

    @Bean
    public Binding binding001(){

        return BindingBuilder.bind(queue001()).to(exchange001()).with("spring.#");
    }

    /**
     * 定义RabbitTemplate模板
     * @param connectionFactory
     * @return
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory){

        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);

        return rabbitTemplate;
    }

    /**
     * 定义SimpleMessageListenerContainer
     * @param connectionFactory
     * @return
     */
    @Bean
    public SimpleMessageListenerContainer simpleMessageListenerContainer(ConnectionFactory connectionFactory){

        SimpleMessageListenerContainer simpleMessageListenerContainer = new SimpleMessageListenerContainer(connectionFactory);

        //监控多个队列
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
                return s+"_"+ UUID.randomUUID().toString();
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

        //使用适配器
        MessageListenerAdapter messageListenerAdapter = new MessageListenerAdapter(new MessageDelegate());
        //指定默认的处理方法
        messageListenerAdapter.setDefaultListenerMethod("consumeMessage");
        //放入适配器
        simpleMessageListenerContainer.setMessageListener(messageListenerAdapter);
        //添加一个转化器:从字节数组转换为String
        simpleMessageListenerContainer.setMessageConverter(new TextMessageConverter());


         //TODO 适配器方式:我们的队列名称和方法名称也可以进行一一的匹配
        /*MessageListenerAdapter messageListenerAdapter = new MessageListenerAdapter(new MessageDelegate());
        Map<String,String> queueOrTagMethodName = new HashMap<>();
        queueOrTagMethodName.put("exchange","routingKey");

        messageListenerAdapter.setQueueOrTagToMethodName(queueOrTagMethodName);
        simpleMessageListenerContainer.setMessageListener(messageListenerAdapter);*/
        return simpleMessageListenerContainer;
    }

}
