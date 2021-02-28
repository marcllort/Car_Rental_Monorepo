package apiservice.car.handler.email.model;

import apiservice.car.handler.HandlerRequest;
import apiservice.car.model.Service;
import lombok.Data;

@Data
public class EmailHandlerRequest extends HandlerRequest {

    private String Flow;
    private Service Service;
    private String Price;
    private String Drivers;
    private String Company;

}
