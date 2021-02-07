package main

import (
	"email/Database"
	"email/RabbitMQ"
	"os"
)

func main() {

	creds := os.Getenv("CREDS")
	dbpass := os.Getenv("SECRET_DB")
	db := Database.CreateConnection(creds, dbpass)
	Database.GetAllServices(db)
	RabbitMQ.Connect()

}
