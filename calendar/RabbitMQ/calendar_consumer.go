package RabbitMQ

import (
	"calendar/CalendarAPI"
	"calendar/Database"
	"calendar/Model"
	"encoding/json"
	"fmt"
	"google.golang.org/api/calendar/v3"
	"gorm.io/gorm"
	"time"
)

func Consume(body string, db *gorm.DB) string {
	var response string
	excludeEmails := []string{"es.spain#holiday@group.v.calendar.google.com", "addressbook#contacts@group.v.calendar.google.com"}

	var request Model.CalendarRequest
	json.Unmarshal([]byte(body), &request)

	calendarClient := CalendarAPI.GetCalendarClient("YOPKsz7f1ITbC1V8WES81CTf12H3")

	switch request.Flow {
	case "eventsMonth":
		response = getEventsMonth(request, calendarClient, excludeEmails)
	case "freeDrivers":
		response = getFreeDrivers(request, calendarClient, excludeEmails)
	case "newService":
		response = createNewServiceDB(db, request)
	case "confirmService":
		response = confirmService(db, calendarClient, request)
	case "driverSetup":
		response = setupDrivers(calendarClient, excludeEmails, db)
	default:
		fmt.Print("default")

	}

	return response
}

func setupDrivers(calendarClient *calendar.Service, excludeEmails []string, db *gorm.DB) string {
	fmt.Print("driverSetup")
	emails := CalendarAPI.GetDriversEmail(calendarClient, excludeEmails)
	Database.CreateDriverFromList(db, emails)

	return "Drivers successfully created!"
}

func confirmService(db *gorm.DB, calendarClient *calendar.Service, request Model.CalendarRequest) string {
	fmt.Print("confirmService")

	var summary string
	driver := Database.GetDriver(db, request.Service.DriverId)
	startTime := request.Service.ServiceDatetime
	duration, _ := time.ParseDuration("1h30m")
	if driver.Name != "default" {
		summary = driver.Name
	} else {
		summary = driver.Email
	}
	summary = summary + ": " + request.Service.Origin + " - " + request.Service.Destination
	CalendarAPI.CreateCalendarEvent(calendarClient, summary, request.Service.Origin, request.Service.Description, driver.Email, startTime, duration)
	service := Database.UpdateConfirmedTime(db, request.Service.ServiceId)

	return "Service with ID:" + string(service.ServiceId) + " confirmed successfully!"
}

func createNewServiceDB(db *gorm.DB, request Model.CalendarRequest) string {
	fmt.Print("newService")
	service := Database.CreateService(db, request.Service)

	return "Service with ID:" + string(service.ServiceId) + " confirmed successfully!"
}

func getFreeDrivers(request Model.CalendarRequest, calendarClient *calendar.Service, excludeEmails []string) string {
	fmt.Print("freeDrivers")
	startTime := request.Service.ServiceDatetime
	duration, _ := time.ParseDuration("1h30m")
	drivers := CalendarAPI.GetFreeDrivers(calendarClient, startTime, duration, excludeEmails)

	driversJson, err := json.Marshal(drivers)
	if err != nil {
		fmt.Println(err)
	}

	return string(driversJson)
}

func getEventsMonth(request Model.CalendarRequest, calendarClient *calendar.Service, excludeEmails []string) string {
	fmt.Print("eventsMonth\n")
	startTime := request.Service.ServiceDatetime
	duration, _ := time.ParseDuration("720h")
	endTime := startTime.Add(duration)
	events := CalendarAPI.GetEvents(calendarClient, request.Service.ServiceDatetime.Format(time.RFC3339), endTime.Format(time.RFC3339), excludeEmails)
	fmt.Print(events)
	eventsJson, err := json.Marshal(events)
	if err != nil {
		fmt.Println(err)
	}

	return string(eventsJson)
}
