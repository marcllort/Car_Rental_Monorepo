package apiservice.car.handler.calendar.model;

import apiservice.car.handler.HandlerRequest;
import apiservice.car.model.Service;
import lombok.Data;

@Data
public class CalendarHandlerRequest extends HandlerRequest {

    private String Flow;
    private Service Service;

}
