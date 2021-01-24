package apiservice.car.controller.handler.calendar;

import apiservice.car.controller.handler.RabbitMQDirectConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;


public abstract class RetrieveCalendarProducerAbstract {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RabbitMQDirectConfig rabbitMQDirectConfig;

    public String produce(String request) {
        Object response = rabbitTemplate.convertSendAndReceive(rabbitMQDirectConfig.EXCHANGE, rabbitMQDirectConfig.LEGAL_KEY, request);
        return response.toString();
    }

}
