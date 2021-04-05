package orchestrator.business;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import orchestrator.handler.RabbitMQDirectConfig;
import orchestrator.handler.calendar.RetrieveCalendarHandler;
import orchestrator.handler.calendar.model.CalendarHandlerRequest;
import orchestrator.handler.calendar.model.CalendarHandlerResponse;
import orchestrator.model.FreeDriversResponse;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class OrchestratorConsumer {

    @Autowired
    private RetrieveCalendarHandler calendarHandler;

    @Autowired
    private PriceCalculator priceCalculator;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private Cronjob cronjob;

    @RabbitListener(queues = RabbitMQDirectConfig.ORCHESTRATOR_QUEUE)
    public String listen(String input) throws IOException {
        CalendarHandlerResponse response = new CalendarHandlerResponse();
        CalendarHandlerRequest request = mapper.readValue(input, CalendarHandlerRequest.class);

        switch (request.getFlow()) {
            case "newService":
                updateFreeDrivers(request);
                updatePrice(request);
                response = (CalendarHandlerResponse) calendarHandler.handle(request);
                break;
            case "confirmService":
                response = (CalendarHandlerResponse) calendarHandler.handle(request);
                break;
            case "modifyService":
                updatePrice(request);
                response = (CalendarHandlerResponse) calendarHandler.handle(request);
                break;
            case "serviceInvoice":
                cronjob.runCronjob();
                response.setText("Running cronjob...");
                break;
            default:

                response = (CalendarHandlerResponse) calendarHandler.handle(request);
                break;
        }
        return response.getText();
    }

    private void updateFreeDrivers(CalendarHandlerRequest request) throws JsonProcessingException {
        CalendarHandlerRequest freeDriversRequest = new CalendarHandlerRequest();
        freeDriversRequest.setService(request.getService());
        freeDriversRequest.setUserId(request.getUserId());
        CalendarHandlerResponse freeDrivers = null;
        freeDriversRequest.setFlow("freeDrivers");
        freeDrivers = (CalendarHandlerResponse) calendarHandler.handle(freeDriversRequest);
        FreeDriversResponse freeDriversResponse = mapper.readValue(freeDrivers.getText(), FreeDriversResponse.class);
        request.getService().setDriverId(freeDriversResponse.getDriversIds().get(0));
    }

    private void updatePrice(CalendarHandlerRequest request) throws IOException {
        float price;
        price = priceCalculator.generatePrice(request);
        request.getService().setBasePrice(price);
    }

}
