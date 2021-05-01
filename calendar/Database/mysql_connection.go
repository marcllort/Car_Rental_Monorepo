package Database

import (
	"calendar/Model"
	"calendar/Utils"
	"encoding/json"
	"fmt"
	_ "github.com/go-sql-driver/mysql"
	"gorm.io/driver/mysql"
	"gorm.io/gorm"
	"time"
)

type MySQLConnection struct {
}

func CreateConnection(creds, dbpass string) *gorm.DB {

	dbURL := Utils.ReadCredentials(creds, dbpass)

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
	printServices(services)

	return services
}

func Summary(db *gorm.DB) string {

	endMonth := time.Now()
	monthIncome := make([]int, 12)
	totalIncome := 0
	for i := 0; i < 12; i++ {
		startMonth := endMonth.AddDate(0, -1, 0)
		var services []Model.ServiceView
		db.Table("service_view").Where("ServiceDatetime BETWEEN ? AND ?", startMonth, endMonth).Find(&services)

		for _, service := range services {
			monthIncome[i] = monthIncome[i] + int(service.BasePrice)
		}
		totalIncome = totalIncome + monthIncome[i]
		endMonth = startMonth
	}

	var unconfirmedEvents int64
	db.Table("service_view").Where("ConfirmedDatetime is NULL").Count(&unconfirmedEvents)

	var summary Model.Summary

	summary.ActualMonthIncome = monthIncome[0]
	summary.MonthlyIncome = monthIncome
	summary.AnualIncome = totalIncome
	summary.UnconfirmedEvents = int(unconfirmedEvents)

	json, _ := json.Marshal(summary)

	return string(json)
}

// Get all events X month X driver/drivers

func getEventsMonth(db *gorm.DB, month string) []Model.ServiceView {
	layout := "2006-01-02"
	startTime, err := time.Parse(layout, month)
	if err != nil {
		fmt.Println(err)
	}
	fmt.Println(startTime)

	endMonth := startTime.AddDate(0, 1, 0)
	var services []Model.ServiceView
	db.Table("service_view").Where("ServiceDatetime BETWEEN ? AND ?", startTime, endMonth).Find(&services)
	printServices(services)

	return services
}

// Get all events for X client
func GetAllServicesClient(db *gorm.DB, clientId int) []Model.ServiceView {
	var services []Model.ServiceView

	db.Table("service_view").Where("ClientId = ?", clientId).Find(&services)
	printServices(services)

	return services
}

// Get all unpaid events
func GetAllUnpaidServices(db *gorm.DB) []Model.ServiceView {
	var services []Model.ServiceView

	db.Table("service_view").Where("PayedDatetime is NULL").Find(&services)
	printServices(services)

	return services
}

// Get all unconfirmed events
func GetAllUnconfirmedServices(db *gorm.DB) []Model.ServiceView {
	var services []Model.ServiceView

	db.Table("service_view").Where("ConfirmedDatetime is NULL").Find(&services)
	printServices(services)

	return services
}

func GetEventById(db *gorm.DB, eventId int) Model.Service {
	var service Model.Service

	db.Table("Service").Find(&service, eventId)

	return service
}

func GetDriver(db *gorm.DB, driverId int) Model.DriverUser {
	var driver Model.DriverUser

	db.Table("DriverUser").Find(&driver, driverId)

	return driver
}

func GetDriverByEmail(db *gorm.DB, emailDriver string) Model.DriverUser {
	var driver Model.DriverUser

	db.Table("DriverUser").Where("email = ?", emailDriver).Find(&driver)

	return driver
}

// Get free drivers at X time
func GetFreeDrivers(db *gorm.DB, startTimeString string, endTimeString string) []Model.DriverUser {
	var drivers []Model.DriverUser
	layout := "2006-01-02T15:04:05.000Z"
	startTime, err := time.Parse(layout, startTimeString)
	endTime, err := time.Parse(layout, endTimeString)
	if err != nil {
		fmt.Println(err)
	}
	fmt.Println(startTime)

	subquery := db.Table("service_view").Where("ServiceDatetime BETWEEN ? AND ?", startTime, endTime).Find(&drivers)
	db.Table("DriverUser").Where("NOT IN ?", subquery).Find(&drivers)

	return drivers
}

func CreateService(db *gorm.DB, service Model.Service) int {
	result := db.Table("Service").Omit("service_id", "confirmed_datetime").Create(&service)

	if result.Error != nil {
		panic(result.Error)
	}
	var created Model.Service

	db.Table("Service").Last(&created)
	return created.ServiceId
}

func CreateDriverFromList(db *gorm.DB, emails []string) {
	var driver Model.DriverUser
	driver.Name = "default"
	driver.Country = "Spain"
	driver.Role = "Driver"
	driver.Phone = "none"

	for _, email := range emails {
		driver.Email = email
		createDriverUser(db, driver)
	}
}

func createDriverUser(db *gorm.DB, driver Model.DriverUser) int {
	result := db.Table("DriverUser").Omit("user_id").Create(&driver)
	var created Model.DriverUser
	db.Table("DriverUser").Last(&created)

	if result.Error != nil {
		panic(result.Error)
	}

	return created.UserId
}

func CreateClientUser(db *gorm.DB, client Model.ClientUser) int {
	result := db.Table("ClientUser").Omit("user_id").Create(&client)

	var created Model.ClientUser
	db.Table("ClientUser").Last(&created)

	if result.Error != nil {
		panic(result.Error)
	}

	return created.UserId
}

func UpdateService(db *gorm.DB, service Model.Service) Model.Service {

	db.Table("Service").Where("service_id = ?", service.ServiceId).Save(service)

	return service
}

// Update confirmed time
func UpdateConfirmedTime(db *gorm.DB, service Model.Service) Model.Service {
	t := time.Now()
	service.ConfirmedDatetime = &t
	db.Table("Service").Where("service_id = ?", service.ServiceId).Model(&service).Updates(Model.Service{ConfirmedDatetime: service.ConfirmedDatetime, CalendarEvent: service.CalendarEvent})

	return service
}

// Update payed time
func updatePayedTime(db *gorm.DB, serviceId int) Model.Service {
	var service Model.Service
	db.Model(&service).Update("PayedDatetime", time.Now())

	return service
}

// Update event (origin, destination, driver, description, serviceDateTime, calendarEvent, basePrice, extraPrice, Passengers, SpecialNeeds)
func updateEvent(db *gorm.DB, service Model.Service) Model.Service {
	db.Save(&service)

	return service
}

// Update driver info
func updateDriver(db *gorm.DB, driver Model.DriverUser) Model.DriverUser {
	db.Save(&driver)

	return driver
}

// Update client info
func updateClient(db *gorm.DB, client Model.ClientUser) Model.ClientUser {
	db.Save(&client)

	return client
}

// Update driver for X service (receive service id, plus driver id)
func updateDriverForService(db *gorm.DB, driverId int, serviceId int) Model.Service {
	var service Model.Service
	db.Table("Service").Where("ServiceId = ?", serviceId).Find(&service)
	service.DriverId = driverId

	db.Save(&service)

	return service
}

func printServices(services []Model.ServiceView) {
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
}
