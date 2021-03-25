package orchestrator.handler.legal.model;

import lombok.Data;
import orchestrator.handler.HandlerResponse;

@Data
public class LegalHandlerResponse extends HandlerResponse {

    private String text;

}
