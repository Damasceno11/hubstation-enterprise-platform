package br.com.damasceno.postodecombustivel.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
public class RabbitMQConfig {

    public static final String FIDELIDADE_QUEUE = "fidelidade.v1.novo-cliente";

    @Bean
    public Queue fidelidadeQueue() {
        // A fila sobrevive se o RabbitMQ reiniciar
        return new Queue(FIDELIDADE_QUEUE, true);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
