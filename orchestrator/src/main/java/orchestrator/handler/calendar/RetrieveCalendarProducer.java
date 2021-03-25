package orchestrator.handler.calendar;

import orchestrator.handler.RabbitMQDirectConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RetrieveCalendarProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public String produce(String request) {
        Object response = rabbitTemplate.convertSendAndReceive(RabbitMQDirectConfig.EXCHANGE, RabbitMQDirectConfig.CALENDAR_KEY, request);
        return response.toString();
    }

}
