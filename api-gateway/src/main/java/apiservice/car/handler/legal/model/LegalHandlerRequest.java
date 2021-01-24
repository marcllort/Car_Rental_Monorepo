package apiservice.car.handler.legal.model;

import apiservice.car.handler.HandlerRequest;
import lombok.Data;

@Data
public class LegalHandlerRequest extends HandlerRequest {

    private String text;

}
