package apiservice.car.handler;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class RabbitMQDirectConfig {

    public static final String CALENDAR_QUEUE = "calendar-queue";
    public static final String LEGAL_QUEUE = "legal-queue";
    public static final String EMAIL_QUEUE = "email-queue";
    public static final String CALENDAR_KEY = "calendar-key";
    public static final String LEGAL_KEY = "legal-key";
    public static final String EMAIL_KEY = "email-key";
    public static final String EXCHANGE = "direct-exchange";

    @Autowired
    private AmqpAdmin amqpAdmin;


    @PostConstruct
    public void createQueues() {
        amqpAdmin.declareQueue(new Queue(CALENDAR_QUEUE, false));
        amqpAdmin.declareQueue(new Queue(LEGAL_QUEUE, false));
        amqpAdmin.declareQueue(new Queue(EMAIL_QUEUE, false));
    }

    @Bean
    Queue calendarQueue() {
        return new Queue(CALENDAR_QUEUE, false);
    }

    @Bean
    Queue legalQueue() {
        return new Queue(LEGAL_QUEUE, false);
    }

    @Bean
    Queue emailQueue() {
        return new Queue(EMAIL_QUEUE, false);
    }

    @Bean
    DirectExchange exchange() {
        return new DirectExchange(EXCHANGE);
    }

    @Bean
    Binding calendarBinding(Queue calendarQueue, DirectExchange exchange) {
        return BindingBuilder.bind(calendarQueue).to(exchange).with("calendar-key");
    }

    @Bean
    Binding legalBinding(Queue legalQueue, DirectExchange exchange) {
        return BindingBuilder.bind(legalQueue).to(exchange).with("legal-key");
    }

    @Bean
    Binding emailBinding(Queue emailQueue, DirectExchange exchange) {
        return BindingBuilder.bind(emailQueue).to(exchange).with("email-key");
    }

}