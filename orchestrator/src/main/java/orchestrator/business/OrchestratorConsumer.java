package orchestrator.business;

import orchestrator.handler.RabbitMQDirectConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class OrchestratorConsumer {

    @RabbitListener(queues = RabbitMQDirectConfig.ORCHESTRATOR_QUEUE)
    public void listen(String in) {
        System.out.println("Message read from myQueue : " + in);
    }

}
