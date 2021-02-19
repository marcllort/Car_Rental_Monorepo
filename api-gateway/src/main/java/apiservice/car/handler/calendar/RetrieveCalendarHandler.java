package apiservice.car.handler.calendar;

import apiservice.car.handler.EndpointHandler;
import apiservice.car.handler.HandlerRequest;
import apiservice.car.handler.HandlerResponse;
import apiservice.car.handler.calendar.model.CalendarHandlerRequest;
import apiservice.car.handler.calendar.model.CalendarHandlerResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
        System.out.println(json);
        String response = producer.produce(json);
        CalendarHandlerResponse calendarHandlerResponse = new CalendarHandlerResponse();
        calendarHandlerResponse.setText(response);
        return calendarHandlerResponse;
    }

}
