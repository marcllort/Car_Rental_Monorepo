package apiservice.car.controller.handler.calendar.model;

import apiservice.car.controller.handler.HandlerRequest;
import lombok.Data;

@Data
public class CalendarHandlerRequest extends HandlerRequest {

    private String text;

}
