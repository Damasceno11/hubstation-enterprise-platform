package br.com.damasceno.notificacao.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

  // Constante para garantir consistência
  public static final String FIDELIDADE_QUEUE = "fidelidade.v1.novo-cliente";

  @Bean
  public MessageConverter jsonMessageConverter() {
    return new Jackson2JsonMessageConverter();
  }

  // O consumidor também garante que a fila existe!
  @Bean
  public Queue fidelidadeQueue() {
    return new Queue(FIDELIDADE_QUEUE, true);
  }
}
