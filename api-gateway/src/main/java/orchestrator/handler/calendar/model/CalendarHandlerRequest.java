package orchestrator.handler.calendar.model;

import lombok.Data;
import orchestrator.handler.HandlerRequest;
import orchestrator.model.Service;

@Data
public class CalendarHandlerRequest extends HandlerRequest {

    private String Flow;
    private Service Service;
    private orchestrator.model.Client Client;

}
