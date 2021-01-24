package apiservice.car.handler.email.model;

import apiservice.car.handler.HandlerRequest;
import lombok.Data;

@Data
public class EmailHandlerRequest extends HandlerRequest {

    private String text;

}
