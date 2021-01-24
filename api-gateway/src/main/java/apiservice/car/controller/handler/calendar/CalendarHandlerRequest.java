package apiservice.car.controller.handler.calendar;

import apiservice.car.controller.handler.HandlerRequest;
import lombok.Data;

@Data
public class CalendarHandlerRequest extends HandlerRequest {

    private String text;

}
