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

	switch request.Flow {
	case "eventsMonth":
		calendarClient := CalendarAPI.GetCalendarClient(request.UserId)
		response = getEventsMonth(calendarClient, request, excludeEmails)
	case "eventById":
		response = getEventById(db, request)
	case "freeDrivers":
		calendarClient := CalendarAPI.GetCalendarClient(request.UserId)
		response = getFreeDrivers(db, calendarClient, request, excludeEmails)
	case "newService":
		calendarClient := CalendarAPI.GetCalendarClient(request.UserId)
		response = createNewServiceDB(db, calendarClient, request)
	case "confirmService":
		calendarClient := CalendarAPI.GetCalendarClient(request.UserId)
		response = confirmService(db, calendarClient, request)
	case "driverSetup":
		calendarClient := CalendarAPI.GetCalendarClient(request.UserId)
		response = setupDrivers(db, calendarClient, excludeEmails)
	case "modifyService":
		calendarClient := CalendarAPI.GetCalendarClient(request.UserId)
		response = modifyService(db, calendarClient, request, excludeEmails)
	case "summary":
		response = summary(db)
	default:
		fmt.Print("default")
		response = "CALENDAR: Error - Unknown flow"
	}

	return response
}

func summary(db *gorm.DB) string {
	fmt.Print("summary")
	return Database.Summary(db)
}

func setupDrivers(db *gorm.DB, calendarClient *calendar.Service, excludeEmails []string) string {
	fmt.Print("driverSetup")
	emails := CalendarAPI.GetDriversEmail(calendarClient, excludeEmails)
	Database.CreateDriverFromList(db, emails)

	return "Drivers successfully created!"
}

func confirmService(db *gorm.DB, calendarClient *calendar.Service, request Model.CalendarRequest) string {
	fmt.Print("confirmService")

	//createCalendarEvent(db, calendarClient, request)

	Database.UpdateConfirmedTime(db, request.Service)

	return "Service with confirmed successfully!"
}

func createCalendarEvent(db *gorm.DB, calendarClient *calendar.Service, request Model.CalendarRequest, id int) {
	var summary string
	driver := Database.GetDriver(db, request.Service.DriverId)
	startTime := request.Service.ServiceDatetime
	duration, _ := time.ParseDuration("1h30m")
	if driver.Name != "default" {
		summary = driver.Name
	} else {
		summary = driver.Email
	}

	idString := strconv.Itoa(id)
	summary = "[" + idString + "]" + summary + ": " + request.Service.Origin + " - " + request.Service.Destination
	request.Service.CalendarEvent = CalendarAPI.CreateCalendarEvent(calendarClient, summary, request.Service.Origin, request.Service.Description, driver.Email, startTime, duration)
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

func createNewServiceDB(db *gorm.DB, calendarClient *calendar.Service, request Model.CalendarRequest) string {
	fmt.Print("newService")
	id := Database.CreateClientUser(db, request.Client)
	request.Service.ClientId = id
	idService := Database.CreateService(db, request.Service)

	createCalendarEvent(db, calendarClient, request, idService)

	return "Service created successfully!"
}

func getFreeDrivers(db *gorm.DB, calendarClient *calendar.Service, request Model.CalendarRequest, excludeEmails []string) string {
	fmt.Print("freeDrivers")
	startTime := request.Service.ServiceDatetime
	duration, _ := time.ParseDuration("1h30m")
	driversEmails, driversids := CalendarAPI.GetFreeDrivers(db, calendarClient, startTime, duration, excludeEmails)
	var freeDrivers Model.FreeDriversResponse
	freeDrivers.DriversIds = driversids
	freeDrivers.DriversNames = driversEmails

	driversJson, err := json.Marshal(freeDrivers)
	if err != nil {
		fmt.Println(err)
	}

	return string(driversJson)
}

func getEventsMonth(calendarClient *calendar.Service, request Model.CalendarRequest, excludeEmails []string) string {
	fmt.Print("eventsMonth\n")
	startTime := request.Service.ServiceDatetime
	duration, _ := time.ParseDuration("72000h") //it will return all the events
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
