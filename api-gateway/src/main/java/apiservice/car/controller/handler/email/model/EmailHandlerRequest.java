package apiservice.car.controller.handler.email.model;

import apiservice.car.controller.handler.HandlerRequest;
import lombok.Data;

@Data
public class EmailHandlerRequest extends HandlerRequest {

    private String text;

}
