package apiservice.car.business;

import apiservice.car.handler.calendar.RetrieveCalendarHandler;
import apiservice.car.handler.calendar.model.CalendarHandlerRequest;
import apiservice.car.handler.calendar.model.CalendarHandlerResponse;
import apiservice.car.model.CalendarEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    private ObjectMapper objectMapper;

    @Scheduled(fixedDelay = 10000)
    public void runCronjob() throws JsonProcessingException {

        //Every day retrieve the calendar events and start async tasks for the invoices and route papers needed
        CalendarHandlerRequest request = generateMockCalendarRequest();
        CalendarHandlerResponse response = (CalendarHandlerResponse) calendarHandler.handle(request);

        List<CalendarEvent> listEvents = objectMapper.readValue(response.getText(), new TypeReference<List<CalendarEvent>>() {
        });


        for (CalendarEvent event : listEvents) {
            if (isInvoice(event)) {
                System.out.println("Starting invoice flow for service X");
                invoiceFlow();
            } else if (isRoutePaper(event)) {
                System.out.println("Starting route paper flow for service X");
                routePaperFlow();
            }
        }
    }

    private boolean isRoutePaper(CalendarEvent event) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(event.getOriginalStartTime().getDate(), formatter);
        ZonedDateTime startTime = date.atStartOfDay(ZoneId.systemDefault());

        return startTime.isBefore(ZonedDateTime.now()) && startTime.isAfter(ZonedDateTime.now().minusDays(1));
    }

    private boolean isInvoice(CalendarEvent event) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(event.getOriginalStartTime().getDate(), formatter);
        ZonedDateTime startTime = date.atStartOfDay(ZoneId.systemDefault());

        return startTime.isBefore(ZonedDateTime.now().plusDays(1)) && startTime.isAfter(ZonedDateTime.now());
    }

    @Async
    void routePaperFlow() throws JsonProcessingException {
        System.out.println("Executing paper flow");

    }

    @Async
    void invoiceFlow() {
        System.out.println("Executing invoice flow");

    }

    private CalendarHandlerRequest generateMockCalendarRequest() {
        CalendarHandlerRequest calendarHandlerRequest = new CalendarHandlerRequest();
        ZonedDateTime zdt = ZonedDateTime.now();
        apiservice.car.model.Service service = new apiservice.car.model.Service();
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
