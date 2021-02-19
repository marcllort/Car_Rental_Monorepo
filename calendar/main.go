package main

import (
	"calendar/CalendarAPI"
	"calendar/Database"
	"calendar/RabbitMQ"
	"os"
)

func main() {

	creds := os.Getenv("CREDS")
	dbpass := os.Getenv("SECRET_DB")

	db := Database.CreateConnection(creds, dbpass)
	calendarClient := CalendarAPI.GetCalendarClient("YOPKsz7f1ITbC1V8WES81CTf12H3")

	RabbitMQ.Connect(db, calendarClient)
	//Database.GetAllServices(db)

	//startTime := time.Date(2021, 2, 12, 12, 0, 0, 0, time.UTC)
	//endTime := time.Date(2021, 3, 1, 0, 0, 0, 0, time.UTC)
	//excludeEmails := []string{"es.spain#holiday@group.v.calendar.google.com", "addressbook#contacts@group.v.calendar.google.com"}
	//duration, _ := time.ParseDuration("4h30m")

	//CalendarAPI.GetEvents(calendarClient, startTime.Format(time.RFC3339), endTime.Format(time.RFC3339), excludeEmails)
	//CalendarAPI.GetFreeDrivers(calendarClient, startTime, duration, excludeEmails)
	//CalendarAPI.CreateCalendarEvent(calendarClient, "summary", "location", "test description", "testdriver@gmail.com", startTime, duration)
}
