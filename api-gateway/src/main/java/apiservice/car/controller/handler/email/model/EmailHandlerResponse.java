package apiservice.car.controller.handler.email.model;

import apiservice.car.controller.handler.HandlerResponse;
import lombok.Data;

@Data
public class EmailHandlerResponse extends HandlerResponse {

    private String text;

}
