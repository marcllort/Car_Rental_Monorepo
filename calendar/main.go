package main

import (
	"calendar/CalendarAPI"
	"calendar/Database"
	"calendar/RabbitMQ"
	"os"
	"time"
)

func main() {

	creds := os.Getenv("CREDS")
	dbpass := os.Getenv("SECRET_DB")
	db := Database.CreateConnection(creds, dbpass)
	Database.GetAllServices(db)
	firestore := CalendarAPI.ConnectFirestore()

	_, calendarClient := CalendarAPI.GetCalendarClient(firestore, "YOPKsz7f1ITbC1V8WES81CTf12H3")

	startTime := time.Date(2021, 1, 1, 0, 0, 0, 0, time.UTC).Format(time.RFC3339)
	endTime := time.Date(2021, 3, 1, 0, 0, 0, 0, time.UTC).Format(time.RFC3339)

	excludeEmails := []string{"es.spain#holiday@group.v.calendar.google.com", "addressbook#contacts@group.v.calendar.google.com"}
	CalendarAPI.GetEvents(calendarClient, startTime, endTime, excludeEmails)
	RabbitMQ.Connect(firestore)

}
