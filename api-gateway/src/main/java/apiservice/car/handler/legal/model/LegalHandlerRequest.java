package apiservice.car.handler.legal.model;

import apiservice.car.handler.HandlerRequest;
import apiservice.car.model.Service;
import lombok.Data;

@Data
public class LegalHandlerRequest extends HandlerRequest {

    private String Flow;
    private Service Service;

}
