package apiservice.car.handler.legal;

import apiservice.car.handler.EndpointHandler;
import apiservice.car.handler.HandlerRequest;
import apiservice.car.handler.HandlerResponse;
import apiservice.car.handler.legal.model.LegalHandlerRequest;
import apiservice.car.handler.legal.model.LegalHandlerResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
