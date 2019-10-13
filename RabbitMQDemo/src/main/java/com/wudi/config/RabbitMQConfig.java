package com.wudi.config;


import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
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
}
