package apiservice.car.handler.calendar;

import apiservice.car.handler.EndpointHandler;
import apiservice.car.handler.HandlerRequest;
import apiservice.car.handler.HandlerResponse;
import apiservice.car.handler.calendar.model.CalendarHandlerRequest;
import apiservice.car.handler.calendar.model.CalendarHandlerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RetrieveCalendarHandler implements EndpointHandler {

    @Autowired
    private RetrieveCalendarProducer producer;

    @Override
    public HandlerResponse handle(HandlerRequest request) {
        CalendarHandlerRequest calendarHandlerRequest = (CalendarHandlerRequest) request;
        String response = producer.produce(calendarHandlerRequest.getText());
        CalendarHandlerResponse calendarHandlerResponse = new CalendarHandlerResponse();
        calendarHandlerResponse.setText(response);
        return calendarHandlerResponse;
    }

}
