package apiservice.car.handler.legal;

import apiservice.car.handler.RabbitMQDirectConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RetrieveLegalProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public String produce(String request) {
        Object response = rabbitTemplate.convertSendAndReceive(RabbitMQDirectConfig.EXCHANGE, RabbitMQDirectConfig.LEGAL_KEY, request);
        return response.toString();
    }

}
