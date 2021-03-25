package orchestrator.business;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import orchestrator.handler.HandlerRequest;
import orchestrator.handler.calendar.RetrieveCalendarHandler;
import orchestrator.handler.calendar.model.CalendarHandlerRequest;
import orchestrator.handler.calendar.model.CalendarHandlerResponse;
import orchestrator.handler.email.RetrieveEmailHandler;
import orchestrator.handler.email.model.EmailHandlerRequest;
import orchestrator.handler.email.model.EmailHandlerResponse;
import orchestrator.model.CalendarEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class Cronjob {

    @Autowired
    private RetrieveCalendarHandler calendarHandler;

    @Autowired
    private RetrieveEmailHandler emailHandler;

    @Autowired
    private ObjectMapper objectMapper;

    @Scheduled(fixedDelay = 86400000)
    public void runCronjob() throws JsonProcessingException {

        //Every day retrieve the calendar events and start async tasks for the invoices and route papers needed
        CalendarHandlerRequest request = (CalendarHandlerRequest) generateMockCalendarRequest();
        CalendarHandlerResponse response = (CalendarHandlerResponse) calendarHandler.handle(request);

        //CalendarEvent event2 = new CalendarEvent();
        //event2.setSummary("[1]");
        //invoiceFlow(event2);
        List<CalendarEvent> listEvents = objectMapper.readValue(response.getText(), new TypeReference<List<CalendarEvent>>() {
        });

        for (CalendarEvent event : listEvents) {
            if (isInvoice(event)) {
                System.out.println("Starting invoice flow for service X");
                invoiceFlow(event);
            } else if (isRoutePaper(event)) {
                System.out.println("Starting route paper flow for service X");
                routePaperFlow(event);
            }
        }
    }

    private boolean isRoutePaper(CalendarEvent event) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(event.getOriginalStartTime().getDate(), formatter);
        ZonedDateTime startTime = date.atStartOfDay(ZoneId.systemDefault());

        return startTime.isBefore(ZonedDateTime.now()) && startTime.isAfter(ZonedDateTime.now().minusDays(1)) && event.summary.contains("[");
    }

    private boolean isInvoice(CalendarEvent event) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(event.getOriginalStartTime().getDate(), formatter);
        ZonedDateTime startTime = date.atStartOfDay(ZoneId.systemDefault());

        return startTime.isBefore(ZonedDateTime.now().plusDays(1)) && startTime.isAfter(ZonedDateTime.now()) && event.summary.contains("[");
    }

    private int getEventId(String summary) {
        summary = summary.substring(summary.indexOf("[") + 1);
        summary = summary.substring(0, summary.indexOf("]"));

        return Integer.parseInt(summary);
    }

    @Async
    void routePaperFlow(CalendarEvent event) throws JsonProcessingException {
        System.out.println("Executing paper flow");
        int eventId = getEventId(event.summary);
        CalendarHandlerRequest calendarRequest = (CalendarHandlerRequest) generateMockCalendarRequest();
        calendarRequest.getService().setServiceId(eventId);
        calendarRequest.setFlow("eventById");

        //retrieve service by id found in the event summary
        CalendarHandlerResponse calendarResponse = (CalendarHandlerResponse) calendarHandler.handle(calendarRequest);
        orchestrator.model.Service service = objectMapper.readValue(calendarResponse.getText(), orchestrator.model.Service.class);


        //send event to legal (which will send it to email)
        EmailHandlerRequest emailRequest = (EmailHandlerRequest) generateMockLegalRequest();
        emailRequest.setFlow("serviceRoutePaper");
        emailRequest.setService(service);
        EmailHandlerResponse emailResponse = (EmailHandlerResponse) emailHandler.handle(emailRequest);

    }

    @Async
    void invoiceFlow(CalendarEvent event) throws JsonProcessingException {
        System.out.println("Executing invoice flow");
        int eventId = getEventId(event.summary);
        CalendarHandlerRequest calendarRequest = (CalendarHandlerRequest) generateMockCalendarRequest();
        calendarRequest.getService().setServiceId(eventId);
        calendarRequest.setFlow("eventById");

        //retrieve service by id found in the event summary
        CalendarHandlerResponse calendarResponse = (CalendarHandlerResponse) calendarHandler.handle(calendarRequest);
        String json = calendarResponse.getText();
        orchestrator.model.Service service = objectMapper.readValue(json, orchestrator.model.Service.class);


        //send event to legal (which will send it to email)
        EmailHandlerRequest emailRequest = (EmailHandlerRequest) generateMockLegalRequest();
        emailRequest.setFlow("serviceInvoice");
        emailRequest.getService().setServiceId(eventId);
        emailRequest.setCompany("test");  // TODO: Parametrize
        float price = service.getBasePrice() + service.getExtraPrice();
        emailRequest.setPrice(String.valueOf(price));
        emailRequest.setDrivers("testdriver"); // TODO: Parametrize
        EmailHandlerResponse emailResponse = (EmailHandlerResponse) emailHandler.handle(emailRequest);

    }

    private HandlerRequest generateMockCalendarRequest() {
        CalendarHandlerRequest calendarHandlerRequest = new CalendarHandlerRequest();
        ZonedDateTime zdt = ZonedDateTime.now();
        orchestrator.model.Service service = new orchestrator.model.Service();
        service.setServiceId(4);
        service.setOrigin("BCN Airport");
        service.setDestination("Girona Airport");
        service.setClientId(1);
        service.setDriverId(1);
        service.setDescription("Test description");
        service.setServiceDatetime(zdt);
        service.setCalendarEvent("calendarURL");
        service.setPayedDatetime(zdt.plusDays(5));
        service.setBasePrice(12F);
        service.setExtraPrice((float) 0);
        //service.setConfirmedDatetime(zdt.plusDays(2));
        service.setPassengers(3);
        service.setSpecialNeeds("none");

        calendarHandlerRequest.setUserId("YOPKsz7f1ITbC1V8WES81CTf12H3");
        calendarHandlerRequest.setFlow("eventsMonth");
        calendarHandlerRequest.setService(service);

        return calendarHandlerRequest;
    }

    private HandlerRequest generateMockLegalRequest() {
        EmailHandlerRequest calendarHandlerRequest = new EmailHandlerRequest();
        ZonedDateTime zdt = ZonedDateTime.now();
        orchestrator.model.Service service = new orchestrator.model.Service();
        service.setServiceId(4);
        service.setOrigin("BCN Airport");
        service.setDestination("Girona Airport");
        service.setClientId(1);
        service.setDriverId(1);
        service.setDescription("Test description");
        service.setServiceDatetime(zdt);
        service.setCalendarEvent("calendarURL");
        service.setPayedDatetime(zdt.plusDays(5));
        service.setBasePrice(12F);
        service.setExtraPrice((float) 0);
        //service.setConfirmedDatetime(zdt.plusDays(2));
        service.setPassengers(3);
        service.setSpecialNeeds("none");

        calendarHandlerRequest.setUserId("YOPKsz7f1ITbC1V8WES81CTf12H3");
        calendarHandlerRequest.setFlow("eventsMonth");
        calendarHandlerRequest.setService(service);

        return calendarHandlerRequest;
    }

}
