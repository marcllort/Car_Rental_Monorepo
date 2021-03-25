package orchestrator.handler.email.model;

import lombok.Data;
import orchestrator.handler.HandlerResponse;

@Data
public class EmailHandlerResponse extends HandlerResponse {

    private String text;

}
