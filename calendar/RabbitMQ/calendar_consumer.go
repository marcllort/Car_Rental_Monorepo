package RabbitMQ

import (
	"calendar/CalendarAPI"
	"calendar/Database"
	"calendar/Model"
	"encoding/json"
	"fmt"
	"google.golang.org/api/calendar/v3"
	"gorm.io/gorm"
	"strconv"
	"time"
)

func Consume(body string, db *gorm.DB) string {
	var response string
	excludeEmails := []string{"es.spain#holiday@group.v.calendar.google.com", "addressbook#contacts@group.v.calendar.google.com"}

	var request Model.CalendarRequest
	json.Unmarshal([]byte(body), &request)

	calendarClient := CalendarAPI.GetCalendarClient(request.UserId)

	switch request.Flow {
	case "eventsMonth":
		response = getEventsMonth(calendarClient, request, excludeEmails)
	case "eventById":
		response = getEventById(db, request)
	case "freeDrivers":
		response = getFreeDrivers(calendarClient, request, excludeEmails)
	case "newService":
		response = createNewServiceDB(db, request)
	case "confirmService":
		response = confirmService(db, calendarClient, request)
	case "driverSetup":
		response = setupDrivers(db, calendarClient, excludeEmails)
	case "modifyService":
		response = modifyService(db, calendarClient, request, excludeEmails)
	default:
		fmt.Print("default")
		response = "CALENDAR: Error - Unknown flow"
	}

	return response
}

func setupDrivers(db *gorm.DB, calendarClient *calendar.Service, excludeEmails []string) string {
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

	id := strconv.Itoa(request.Service.ServiceId)
	summary = "[" + id + "]" + summary + ": " + request.Service.Origin + " - " + request.Service.Destination
	request.Service.CalendarEvent = CalendarAPI.CreateCalendarEvent(calendarClient, summary, request.Service.Origin, request.Service.Description, driver.Email, startTime, duration)

	Database.UpdateConfirmedTime(db, request.Service)

	return "Service with confirmed successfully!"
}

func modifyService(db *gorm.DB, calendarClient *calendar.Service, request Model.CalendarRequest, excludeEmails []string) string {
	fmt.Print("modifyService")

	var summary string
	driver := Database.GetDriver(db, request.Service.DriverId)
	startTime := request.Service.ServiceDatetime
	duration, _ := time.ParseDuration("1h30m")
	if driver.Name != "default" {
		summary = driver.Name
	} else {
		summary = driver.Email
	}

	id := strconv.Itoa(request.Service.ServiceId)
	summary = "[" + id + "]" + summary + ": " + request.Service.Origin + " - " + request.Service.Destination

	if request.Service.ConfirmedDatetime != nil {
		CalendarAPI.UpdateCalendarEvent(calendarClient, id, summary, request.Service.Origin, request.Service.Description,
			driver.Email, startTime, duration, excludeEmails)
	}
	Database.UpdateService(db, request.Service)

	return "Service with updated successfully!"
}

func createNewServiceDB(db *gorm.DB, request Model.CalendarRequest) string {
	fmt.Print("newService")
	Database.CreateService(db, request.Service)

	return "Service created successfully!"
}

func getFreeDrivers(calendarClient *calendar.Service, request Model.CalendarRequest, excludeEmails []string) string {
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

func getEventsMonth(calendarClient *calendar.Service, request Model.CalendarRequest, excludeEmails []string) string {
	fmt.Print("eventsMonth\n")
	startTime := request.Service.ServiceDatetime
	duration, _ := time.ParseDuration("720h")
	endTime := startTime.Add(duration)
	events, _ := CalendarAPI.GetEvents(calendarClient, startTime.Format(time.RFC3339), endTime.Format(time.RFC3339), excludeEmails)
	fmt.Print(events)
	eventsJson, err := json.Marshal(events)
	if err != nil {
		fmt.Println(err)
	}

	return string(eventsJson)
}

func getEventById(db *gorm.DB, request Model.CalendarRequest) string {
	fmt.Print("eventById\n")
	id := request.Service.ServiceId
	service := Database.GetEventById(db, id)

	json, _ := json.Marshal(service)

	return string(json)
}
