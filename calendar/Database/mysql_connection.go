package Database

import (
	"calendar/Model"
	"calendar/Utils"
	"fmt"
	_ "github.com/go-sql-driver/mysql"
	"gorm.io/driver/mysql"
	"gorm.io/gorm"
)

func CreateConnection(creds, dbpass string) *gorm.DB {

	dbURL := Utils.ReadCredentials(creds+"/creds.json", dbpass)

	db, err := gorm.Open(mysql.Open(dbURL), &gorm.Config{})
	if err != nil {
		panic("failed to connect database")
	}
	fmt.Println("DB is connected!")

	return db
}

func GetAllServices(db *gorm.DB) []Model.ServiceView {

	var services []Model.ServiceView

	db.Table("service_view").Find(&services)

	for _, service := range services {
		fmt.Printf("ID: %d - Origin: %s - Destination: %s - Description: %s - ServiceDateTime: %s - CalendarEvent: %s "+
			"- PayedDateTime: %s \n\t\tBasePrice: %.2f - ExtraPrice: %.2f - ConfirmedDateTime: %s - Passengers: %d - SpecialNeeds: %s \n",
			service.ServiceId, service.Origin, service.Destination, service.Description, service.ServiceDatetime, service.CalendarEvent,
			service.PayedDatetime, service.BasePrice, service.ExtraPrice, service.ConfirmedDatetime, service.Passengers, service.SpecialNeeds)
		fmt.Printf("Client ID: %d - Name: %s - Email: %s - Country: %s - Phone: %s \n", service.ClientId, service.ClientName,
			service.ClientMail, service.ClientCountry, service.ClientPhone)
		fmt.Printf("Driver ID: %d - Name: %s - Email: %s - Country: %s - Phone: %s \n", service.DriverId, service.DriverName,
			service.DriverMail, service.DriverCountry, service.DriverPhone)
	}

	return services
}

func createService(db *gorm.DB, service Model.Service) int {
	result := db.Omit("ClientId").Create(&service)

	if result.Error != nil {
		panic(result.Error)
	}

	return service.ServiceId
}

func createDriverUser(db *gorm.DB, driver Model.DriverUser) int {
	result := db.Omit("DriverId").Create(&driver)

	if result.Error != nil {
		panic(result.Error)
	}

	return driver.UserId
}

func createClientUser(db *gorm.DB, client Model.ClientUser) int {
	result := db.Omit("ClientId").Create(&client)

	if result.Error != nil {
		panic(result.Error)
	}

	return client.UserId
}
