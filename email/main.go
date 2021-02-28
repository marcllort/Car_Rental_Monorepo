package main

import (
	"email/Database"
	"email/Firestore"
	"email/RabbitMQ"
	"os"
)

func main() {

	creds := os.Getenv("CREDS")
	dbpass := os.Getenv("SECRET_DB")
	google_creds := os.Getenv("SECRET_FIREBASE")

	db := Database.CreateConnection(creds, dbpass)
	firestore, ctx := Firestore.CreateFirestoreConnection(google_creds)
	RabbitMQ.SendCalendarEmail("futbolsupplier@gmail.com", "shopifySupplier2019", "mac12llm@gmail.com")

	RabbitMQ.Connect(db, firestore, ctx)
	defer firestore.Close()

	Firestore.GetDocuments(firestore, ctx)
	//Database.GetAllServices(db)

}
