package orchestrator.business;

import orchestrator.handler.calendar.model.CalendarHandlerRequest;
import org.springframework.stereotype.Component;

@Component
public class PriceCalculator {

    public float generatePrice(CalendarHandlerRequest request) {
        float price = 0;

        String origin = request.getService().getOrigin();
        String destination = request.getService().getDestination();

        price = (float) Math.random();

        return price;
    }

}
