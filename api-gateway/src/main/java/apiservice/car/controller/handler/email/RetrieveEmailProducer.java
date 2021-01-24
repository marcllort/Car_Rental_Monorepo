package apiservice.car.controller.handler.email;

import apiservice.car.controller.handler.RabbitMQDirectConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RetrieveEmailProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RabbitMQDirectConfig rabbitMQDirectConfig;

    public String produce(String request) {
        Object response = rabbitTemplate.convertSendAndReceive(rabbitMQDirectConfig.EXCHANGE, rabbitMQDirectConfig.EMAIL_KEY, request);
        return response.toString();
    }

}
