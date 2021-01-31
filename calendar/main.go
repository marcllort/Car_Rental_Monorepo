package main

import (
	"calendar/Database"
	"calendar/RabbitMQ"
	"os"
)

func main() {

	creds := os.Args[1]
	dbpass := os.Args[2]
	_ = Database.CreateConnection(creds, dbpass)

	RabbitMQ.Connect()

}
