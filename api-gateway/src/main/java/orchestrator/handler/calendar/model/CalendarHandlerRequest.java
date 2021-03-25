package orchestrator.handler.calendar.model;

import lombok.Data;
import orchestrator.handler.HandlerRequest;

@Data
public class CalendarHandlerRequest extends HandlerRequest {

    private String Flow;
    private orchestrator.model.Service Service;

}
