package apiservice.car.handler.email;

import apiservice.car.handler.EndpointHandler;
import apiservice.car.handler.HandlerRequest;
import apiservice.car.handler.HandlerResponse;
import apiservice.car.handler.email.model.EmailHandlerRequest;
import apiservice.car.handler.email.model.EmailHandlerResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RetrieveEmailHandler implements EndpointHandler {

    @Autowired
    private RetrieveEmailProducer producer;

    @Autowired
    private ObjectMapper jacksonObjectMapper;

    @Override
    public HandlerResponse handle(HandlerRequest request) throws JsonProcessingException {
        EmailHandlerRequest emailHandlerRequest = (EmailHandlerRequest) request;
        String json = jacksonObjectMapper.writeValueAsString(emailHandlerRequest);

        String response = producer.produce(json);

        EmailHandlerResponse emailHandlerResponse = new EmailHandlerResponse();
        emailHandlerResponse.setText(response);
        return emailHandlerResponse;
    }

}
