package apiservice.car.handler.email;

import apiservice.car.handler.RabbitMQDirectConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RetrieveEmailProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public String produce(String request) {
        Object response = rabbitTemplate.convertSendAndReceive(RabbitMQDirectConfig.EXCHANGE, RabbitMQDirectConfig.EMAIL_KEY, request);
        return response.toString();
    }

}
