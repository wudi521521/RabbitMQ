package com.wudi.config;


import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

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


}
