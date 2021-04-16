package orchestrator.business;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import orchestrator.handler.RabbitMQDirectConfig;
import orchestrator.handler.calendar.RetrieveCalendarHandler;
import orchestrator.handler.calendar.model.CalendarHandlerRequest;
import orchestrator.handler.calendar.model.CalendarHandlerResponse;
import orchestrator.handler.email.RetrieveEmailHandler;
import orchestrator.handler.email.model.EmailHandlerRequest;
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
    private RetrieveEmailHandler emailHandler;

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
                FreeDriversResponse freeDriversResponse = updateFreeDrivers(request);
                updatePrice(request);
                response = (CalendarHandlerResponse) calendarHandler.handle(request);

                emailHandler.handle(generateEmailServiceRequest(request, freeDriversResponse));
                break;
            case "confirmService":
                response = (CalendarHandlerResponse) calendarHandler.handle(request);
                emailHandler.handle(generateEmailConfirmServiceRequest(request));
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

    private EmailHandlerRequest generateEmailServiceRequest(CalendarHandlerRequest request, FreeDriversResponse freeDriversResponse) {
        EmailHandlerRequest handlerRequest = new EmailHandlerRequest();
        handlerRequest.setFlow("serviceRequest");
        handlerRequest.setCompany("Pressicar");
        handlerRequest.setUserId(request.getUserId());
        if (freeDriversResponse.getDriversNames() != null) {
            handlerRequest.setDrivers(freeDriversResponse.getDriversNames().toString());
        } else {
            handlerRequest.setDrivers("No driver suggestions available");
        }
        handlerRequest.setPrice(String.valueOf(request.getService().getBasePrice()));
        handlerRequest.setService(request.getService());
        return handlerRequest;
    }

    private EmailHandlerRequest generateEmailConfirmServiceRequest(CalendarHandlerRequest request) {
        EmailHandlerRequest handlerRequest = new EmailHandlerRequest();
        handlerRequest.setFlow("serviceConfirmed");
        handlerRequest.setCompany("Pressicar");
        handlerRequest.setUserId(request.getUserId());
        handlerRequest.setPrice(String.valueOf(request.getService().getBasePrice()));
        handlerRequest.setService(request.getService());
        return handlerRequest;
    }

    private FreeDriversResponse updateFreeDrivers(CalendarHandlerRequest request) throws JsonProcessingException {
        CalendarHandlerRequest freeDriversRequest = new CalendarHandlerRequest();
        request.setUserId("YOPKsz7f1ITbC1V8WES81CTf12H3");
        freeDriversRequest.setService(request.getService());
        freeDriversRequest.setUserId(request.getUserId());
        CalendarHandlerResponse freeDrivers = null;
        freeDriversRequest.setFlow("freeDrivers");
        freeDrivers = (CalendarHandlerResponse) calendarHandler.handle(freeDriversRequest);
        if (freeDrivers == null) {
            FreeDriversResponse freeDriversResponse = mapper.readValue(freeDrivers.getText(), FreeDriversResponse.class);
            request.getService().setDriverId(freeDriversResponse.getDriversIds().get(0));
        }
        return new FreeDriversResponse();
    }

    private void updatePrice(CalendarHandlerRequest request) throws IOException {
        float price;
        price = priceCalculator.generatePrice(request);
        request.getService().setBasePrice(price);
    }

}
