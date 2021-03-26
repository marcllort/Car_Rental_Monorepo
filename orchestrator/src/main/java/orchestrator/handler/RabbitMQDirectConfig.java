package orchestrator.handler;

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
    public static final String ORCHESTRATOR_QUEUE = "orchestrator-queue";
    public static final String CALENDAR_KEY = "calendar-key";
    public static final String LEGAL_KEY = "legal-key";
    public static final String EMAIL_KEY = "email-key";
    public static final String ORCHESTRATOR_KEY = "orchestrator-key";
    public static final String EXCHANGE = "direct-exchange";

    @Autowired
    private AmqpAdmin amqpAdmin;


    @PostConstruct
    public void createQueues() {
        amqpAdmin.declareQueue(new Queue(CALENDAR_QUEUE, false));
        amqpAdmin.declareQueue(new Queue(LEGAL_QUEUE, false));
        amqpAdmin.declareQueue(new Queue(EMAIL_QUEUE, false));
        amqpAdmin.declareQueue(new Queue(ORCHESTRATOR_QUEUE, false));
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
    Queue orchestratorQueue() {
        return new Queue(ORCHESTRATOR_QUEUE, false);
    }

    @Bean
    DirectExchange exchange() {
        return new DirectExchange(EXCHANGE);
    }

    @Bean
    Binding calendarBinding(Queue calendarQueue, DirectExchange exchange) {
        return BindingBuilder.bind(calendarQueue).to(exchange).with(CALENDAR_KEY);
    }

    @Bean
    Binding legalBinding(Queue legalQueue, DirectExchange exchange) {
        return BindingBuilder.bind(legalQueue).to(exchange).with(LEGAL_KEY);
    }

    @Bean
    Binding emailBinding(Queue emailQueue, DirectExchange exchange) {
        return BindingBuilder.bind(emailQueue).to(exchange).with(EMAIL_KEY);
    }

    @Bean
    Binding orchestratorBinding(Queue orchestratorQueue, DirectExchange exchange) {
        return BindingBuilder.bind(orchestratorQueue).to(exchange).with(ORCHESTRATOR_KEY);
    }

}