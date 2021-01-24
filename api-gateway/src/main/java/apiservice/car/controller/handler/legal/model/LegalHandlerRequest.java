package apiservice.car.controller.handler.legal.model;

import apiservice.car.controller.handler.HandlerRequest;
import lombok.Data;

@Data
public class LegalHandlerRequest extends HandlerRequest {

    private String text;

}
