package orchestrator.handler.legal;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import orchestrator.handler.EndpointHandler;
import orchestrator.handler.HandlerRequest;
import orchestrator.handler.HandlerResponse;
import orchestrator.handler.legal.model.LegalHandlerRequest;
import orchestrator.handler.legal.model.LegalHandlerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RetrieveLegalHandler implements EndpointHandler {

    @Autowired
    private RetrieveLegalProducer producer;

    @Autowired
    private ObjectMapper jacksonObjectMapper;

    @Override
    public HandlerResponse handle(HandlerRequest request) throws JsonProcessingException {
        LegalHandlerRequest legalHandlerRequest = (LegalHandlerRequest) request;
        String json = jacksonObjectMapper.writeValueAsString(legalHandlerRequest);

        String response = producer.produce(json);

        LegalHandlerResponse legalHandlerResponse = new LegalHandlerResponse();
        legalHandlerResponse.setText(response);
        return legalHandlerResponse;
    }

}
