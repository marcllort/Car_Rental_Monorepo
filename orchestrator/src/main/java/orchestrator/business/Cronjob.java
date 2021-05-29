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
import orchestrator.model.ServiceCaps;
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

        List<CalendarEvent> listEvents = objectMapper.readValue(response.getText(), new TypeReference<List<CalendarEvent>>() {
        });

        for (CalendarEvent event : listEvents) {
            if (isInvoice(event)) {
                System.out.println("Starting invoice flow for service " + event.getSummary());
                invoiceFlow(event);
            } else if (isRoutePaper(event)) {
                System.out.println("Starting route paper flow for service " + event.getSummary());
                routePaperFlow(event);
            }
        }
    }

    private boolean isRoutePaper(CalendarEvent event) {
        if (event.getOriginalStartTime() == null && event.getStart().dateTime == null) {
            return false;
        }
        LocalDate date;
        if (event.getOriginalStartTime() == null) {
            String[] parts = event.getStart().dateTime.split("\\+");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
            date = LocalDate.parse(parts[0], formatter);
        } else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            date = LocalDate.parse(event.getOriginalStartTime().getDate(), formatter);
        }
        ZonedDateTime startTime = date.atStartOfDay(ZoneId.systemDefault());

        return startTime.isAfter(ZonedDateTime.now()) && startTime.isBefore(ZonedDateTime.now().plusDays(1)) && event.summary.contains("[");
    }

    private boolean isInvoice(CalendarEvent event) {
        if (event.getOriginalStartTime() == null && event.getStart().dateTime == null) {
            return false;
        }
        LocalDate date;
        if (event.getOriginalStartTime() == null) {
            String[] parts = event.getStart().dateTime.split("\\+");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
            date = LocalDate.parse(parts[0], formatter);
        } else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            date = LocalDate.parse(event.getOriginalStartTime().getDate(), formatter);
        }

        ZonedDateTime startTime = date.atStartOfDay(ZoneId.systemDefault());

        return startTime.plusHours(23).isAfter(ZonedDateTime.now().minusDays(1)) && startTime.plusHours(23).isBefore(ZonedDateTime.now().plusDays(1)) && event.summary.contains("[");
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
        orchestrator.model.ServiceCaps service = objectMapper.readValue(calendarResponse.getText(), orchestrator.model.ServiceCaps.class);


        //send event to legal (which will send it to email)
        EmailHandlerRequest emailRequest = (EmailHandlerRequest) generateMockLegalRequest();
        emailRequest.setFlow("serviceRoutePaper");
        emailRequest.setCompany("Pressicar");
        float price = service.getBasePrice() + service.getExtraPrice();
        emailRequest.setPrice(String.valueOf(price));
        emailRequest.setDrivers("Company driver");
        emailRequest.setService(mapService(service));
        EmailHandlerResponse emailResponse = (EmailHandlerResponse) emailHandler.handle(emailRequest);

    }

    orchestrator.model.Service mapService(ServiceCaps caps) {
        orchestrator.model.Service service = new orchestrator.model.Service();
        service.setServiceId(caps.getServiceId());
        service.setServiceDatetime(caps.getServiceDatetime());
        service.setDriverId(caps.getDriverId());
        service.setCalendarEvent(caps.getCalendarEvent());
        service.setClientId(caps.getClientId());
        service.setDescription(caps.getDescription());
        service.setConfirmedDatetime(caps.getConfirmedDatetime());
        service.setDestination(caps.getDestination());
        service.setOrigin(caps.getOrigin());
        service.setBasePrice(caps.getBasePrice());
        service.setExtraPrice(caps.getExtraPrice());
        service.setPassengers(caps.getPassengers());
        service.setPayedDatetime(caps.getPayedDatetime());
        service.setSpecialNeeds(caps.getSpecialNeeds());
        return service;
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
        orchestrator.model.ServiceCaps service = objectMapper.readValue(json, orchestrator.model.ServiceCaps.class);


        //send event to legal (which will send it to email)
        EmailHandlerRequest emailRequest = (EmailHandlerRequest) generateMockLegalRequest();
        emailRequest.setFlow("serviceInvoice");
        emailRequest.getService().setServiceId(eventId);
        emailRequest.setCompany("Pressicar");
        emailRequest.setService(mapService(service));
        float price = service.getBasePrice() + service.getExtraPrice();
        emailRequest.setPrice(String.valueOf(price));
        emailRequest.setDrivers("Company driver");
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
