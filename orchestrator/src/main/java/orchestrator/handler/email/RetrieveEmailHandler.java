package orchestrator.handler.email;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import orchestrator.handler.EndpointHandler;
import orchestrator.handler.HandlerRequest;
import orchestrator.handler.HandlerResponse;
import orchestrator.handler.email.model.EmailHandlerRequest;
import orchestrator.handler.email.model.EmailHandlerResponse;
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
