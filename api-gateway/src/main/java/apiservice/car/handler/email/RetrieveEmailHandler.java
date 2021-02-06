package apiservice.car.handler.email;

import apiservice.car.handler.EndpointHandler;
import apiservice.car.handler.HandlerRequest;
import apiservice.car.handler.HandlerResponse;
import apiservice.car.handler.email.model.EmailHandlerRequest;
import apiservice.car.handler.email.model.EmailHandlerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RetrieveEmailHandler implements EndpointHandler {

    @Autowired
    private RetrieveEmailProducer producer;

    @Override
    public HandlerResponse handle(HandlerRequest request) {
        EmailHandlerRequest emailHandlerRequest = (EmailHandlerRequest) request;
        String response = producer.produce(emailHandlerRequest.getText());
        EmailHandlerResponse emailHandlerResponse = new EmailHandlerResponse();
        emailHandlerResponse.setText(response);
        return emailHandlerResponse;
    }

}