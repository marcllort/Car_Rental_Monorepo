package Database

import (
	"calendar/Model"
	"calendar/Utils"
	"database/sql"
	"fmt"
	_ "github.com/go-sql-driver/mysql"
	"strings"
)

func CreateConnection(creds, dbpass string) *sql.DB {

	dbURL := Utils.ReadCredentials(creds+"/creds.json", dbpass)

	db, err := sql.Open("mysql", dbURL)
	if err != nil {
		fmt.Println(err.Error())
		defer db.Close()

		err = db.Ping()
		fmt.Println(err)
		if err != nil {
			fmt.Println("MySQL db is not connected")
			fmt.Println(err.Error())
		}
	}
	fmt.Println("DB is connected!")

	return db
}

/*func SelectCountryOriginDest(db *sql.DB, destination string, origin string) Model.Service {

	destination = strings.ReplaceAll(destination, " ", "_")
	origin = strings.ReplaceAll(origin, " ", "_")

	var query strings.Builder
	query.WriteString("SELECT ")
	query.WriteString("*")
	query.WriteString(" FROM Service")
	//query.WriteString(origin)
	// query.WriteString("'")

	finalQuery := query.String()

	selDB, err := db.Query(finalQuery)

	if err != nil {
		panic(err.Error())
	}
	//var country Model.InfoCountry
	var info string

	for selDB.Next() {
		selDB.Scan(&info)

		if err != nil {
			panic(err.Error())
		}
		//	country.Info = info
	}

	//return country

}*/

func ReadData(db *sql.DB) {

	rows, err := db.Query("select * from company_pressicar.service_view")
	if err != nil {
		fmt.Println(err)
		return
	}
	defer rows.Close()
	var services []Model.Service
	var clients []Model.ClientUser
	var drivers []Model.DriverUser

	var service Model.Service
	var client Model.ClientUser
	var driver Model.DriverUser
	for rows.Next() {
		err := rows.Scan(&service.ServiceId, &service.Origin, &service.Destination, &driver.UserId, &driver.Name, &driver.Phone, &driver.Email, &driver.Country,
			&client.UserId, &client.Name, &client.Phone, &client.Email, &client.Country,
			&service.Description, &service.ServiceDatetime, &service.CalendarEvent,
			&service.PayedDatetime, &service.BasePrice, &service.ExtraPrice, &service.ConfirmedDatetime, &service.Passengers, &service.SpecialNeeds)
		if err != nil {
			fmt.Println(err)
			return
		}
		fmt.Printf("ID: %d - Origin: %s - Destination: %s \n", service.ServiceId, service.Origin, service.Destination)
		services = append(services, service)
		fmt.Printf("Client ID: %d - Name: %s - Email: %s \n", client.UserId, client.Name, client.Name)
		clients = append(clients, client)
		fmt.Printf("Driver ID: %d - Name: %s - Email: %s \n", driver.UserId, driver.Name, driver.Name)
		drivers = append(drivers, driver)
	}
	return
}

func ExistsCountry(db *sql.DB, countrySelect string) bool {
	var query strings.Builder
	query.WriteString("SELECT EXISTS(SELECT * FROM PassportInfo WHERE Passport LIKE '")
	query.WriteString(countrySelect)
	query.WriteString("')")

	finalQuery := query.String()

	var exists bool
	row := db.QueryRow(finalQuery)
	err := row.Scan(&exists)

	if err != nil {
		panic(err.Error())
	}

	return exists
}
