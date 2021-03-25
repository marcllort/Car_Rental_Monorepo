package orchestrator.handler.legal.model;

import lombok.Data;
import orchestrator.handler.HandlerRequest;
import orchestrator.model.Service;

@Data
public class LegalHandlerRequest extends HandlerRequest {

    private String Flow;
    private Service Service;

}
