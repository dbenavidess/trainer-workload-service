package com.example.trainerworkloadservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.connection.SingleConnectionFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageType;

import java.util.Map;


@Configuration
@EnableJms
public class JmsConfig {

    @Bean
    public SingleConnectionFactory singleConnectionFactory() {
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory();
        return new SingleConnectionFactory(activeMQConnectionFactory);
    }

    @Bean
    public MappingJackson2MessageConverter jacksonJmsMessageConverter() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setObjectMapper(mapper);
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");

        converter.setTypeIdMappings(Map.of(
                "WorkloadRequest", com.example.trainerworkloadservice.dto.WorkloadRequest.class
        ));

        return converter;
    }

    @Bean
    public JmsTemplate jmsTemplate(SingleConnectionFactory connectionFactory,
                                   MappingJackson2MessageConverter converter) {
        JmsTemplate template = new JmsTemplate(connectionFactory);
        template.setMessageConverter(converter);
        template.setPubSubDomain(false);
        return template;
    }
}