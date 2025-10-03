package udpm.hn.server.infrastructure.integration.rabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Cấu hình RabbitMQ cho hệ thống
 */
@Configuration
public class RabbitMQConfig {

    // Tên các queue
    public static final String QUEUE_BATCH = "batch.queue";
    public static final String QUEUE_NOTIFICATION = "notification.queue";
    
    // Tên các exchange
    public static final String EXCHANGE_DIRECT = "direct.exchange";
    
    // Routing keys
    public static final String ROUTING_KEY_BATCH = "batch.routing.key";
    public static final String ROUTING_KEY_NOTIFICATION = "notification.routing.key";
    
    @Bean
    public Queue batchQueue() {
        return new Queue(QUEUE_BATCH, true);
    }
    
    @Bean
    public Queue notificationQueue() {
        return new Queue(QUEUE_NOTIFICATION, true);
    }
    
    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(EXCHANGE_DIRECT);
    }
    
    @Bean
    public Binding bindingBatch(Queue batchQueue, DirectExchange exchange) {
        return BindingBuilder.bind(batchQueue)
                .to(exchange)
                .with(ROUTING_KEY_BATCH);
    }
    
    @Bean
    public Binding bindingNotification(Queue notificationQueue, DirectExchange exchange) {
        return BindingBuilder.bind(notificationQueue)
                .to(exchange)
                .with(ROUTING_KEY_NOTIFICATION);
    }
    
    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
    
    @Bean
    public AmqpTemplate template(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter());
        return rabbitTemplate;
    }
}
