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
	Database.GetAllServices(db)
	firestore := CalendarAPI.ConnectFirestore()
	//CalendarAPI.GetRefreshToken(firestore, "YOPKsz7f1ITbC1V8WES81CTf12H3")

	CalendarAPI.GetEvents(firestore, "YOPKsz7f1ITbC1V8WES81CTf12H3")
	RabbitMQ.Connect(firestore)

}
