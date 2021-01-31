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

func SelectCountryOriginDest(db *sql.DB, destination string, origin string) Model.Service {

	destination = strings.ReplaceAll(destination, " ", "_")
	origin = strings.ReplaceAll(origin, " ", "_")

	var query strings.Builder
	query.WriteString("SELECT ")
	query.WriteString(destination)
	query.WriteString(" FROM PassportInfo WHERE Passport LIKE '")
	query.WriteString(origin)
	query.WriteString("'")

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
