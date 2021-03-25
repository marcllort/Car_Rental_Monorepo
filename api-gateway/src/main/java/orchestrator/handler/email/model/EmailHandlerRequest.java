package orchestrator.handler.email.model;

import lombok.Data;
import orchestrator.handler.HandlerRequest;

@Data
public class EmailHandlerRequest extends HandlerRequest {

    private String Flow;
    private orchestrator.model.Service Service;
    private String Price;
    private String Drivers;
    private String Company;

}
