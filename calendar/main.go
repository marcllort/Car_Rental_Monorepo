package main

import (
	"calendar/Database"
	"calendar/RabbitMQ"
	"os"
)

func main() {

	creds := os.Getenv("creds")
	dbpass := os.Getenv("dbpass")
	db := Database.CreateConnection(creds, dbpass)
	Database.ReadData(db)
	RabbitMQ.Connect()

}
