package apiservice.car.controller.handler.calendar;

import apiservice.car.controller.handler.EndpointHandler;
import apiservice.car.controller.handler.HandlerRequest;
import apiservice.car.controller.handler.HandlerResponse;
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
