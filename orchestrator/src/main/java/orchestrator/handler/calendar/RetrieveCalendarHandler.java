package orchestrator.handler.calendar;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import orchestrator.handler.EndpointHandler;
import orchestrator.handler.HandlerRequest;
import orchestrator.handler.HandlerResponse;
import orchestrator.handler.calendar.model.CalendarHandlerRequest;
import orchestrator.handler.calendar.model.CalendarHandlerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RetrieveCalendarHandler implements EndpointHandler {

    @Autowired
    private RetrieveCalendarProducer producer;

    @Autowired
    private ObjectMapper jacksonObjectMapper;

    @Override
    public HandlerResponse handle(HandlerRequest request) throws JsonProcessingException {
        CalendarHandlerRequest calendarHandlerRequest = (CalendarHandlerRequest) request;
        String json = jacksonObjectMapper.writeValueAsString(calendarHandlerRequest);

        String response = producer.produce(json);

        CalendarHandlerResponse calendarHandlerResponse = new CalendarHandlerResponse();
        calendarHandlerResponse.setText(response);

        return calendarHandlerResponse;
    }

}
