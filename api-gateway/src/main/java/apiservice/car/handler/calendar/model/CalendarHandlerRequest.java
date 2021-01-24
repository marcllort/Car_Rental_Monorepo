package apiservice.car.handler.calendar.model;

import apiservice.car.handler.HandlerRequest;
import lombok.Data;

@Data
public class CalendarHandlerRequest extends HandlerRequest {

    private String text;

}
