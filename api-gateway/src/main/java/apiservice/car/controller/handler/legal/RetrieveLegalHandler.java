package apiservice.car.controller.handler.legal;

import apiservice.car.controller.handler.EndpointHandler;
import apiservice.car.controller.handler.HandlerRequest;
import apiservice.car.controller.handler.HandlerResponse;
import apiservice.car.controller.handler.legal.model.LegalHandlerRequest;
import apiservice.car.controller.handler.legal.model.LegalHandlerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RetrieveLegalHandler implements EndpointHandler {

    @Autowired
    private RetrieveLegalProducer producer;

    @Override
    public HandlerResponse handle(HandlerRequest request) {
        LegalHandlerRequest legalHandlerRequest = (LegalHandlerRequest) request;
        String response = producer.produce(legalHandlerRequest.getText());
        LegalHandlerResponse legalHandlerResponse = new LegalHandlerResponse();
        legalHandlerResponse.setText(response);
        return legalHandlerResponse;
    }

}
