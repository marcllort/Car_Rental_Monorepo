package orchestrator.handler;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface EndpointHandler {

    HandlerResponse handle(final HandlerRequest request) throws JsonProcessingException;

}
