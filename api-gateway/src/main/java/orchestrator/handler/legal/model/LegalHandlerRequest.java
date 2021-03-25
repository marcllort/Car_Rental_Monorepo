package orchestrator.handler.legal.model;

import lombok.Data;
import orchestrator.handler.HandlerRequest;

@Data
public class LegalHandlerRequest extends HandlerRequest {

    private String Flow;
    private orchestrator.model.Service Service;

}
