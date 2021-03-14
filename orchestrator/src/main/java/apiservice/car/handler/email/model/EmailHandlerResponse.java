package apiservice.car.handler.email.model;

import apiservice.car.handler.HandlerResponse;
import lombok.Data;

@Data
public class EmailHandlerResponse extends HandlerResponse {

    private String text;

}
