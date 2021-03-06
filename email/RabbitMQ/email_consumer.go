package RabbitMQ

import (
	"bytes"
	"cloud.google.com/go/firestore"
	"context"
	"email/Database"
	"email/Model"
	"encoding/json"
	"fmt"
	"gopkg.in/gomail.v2"
	"gorm.io/gorm"
	"io"
	"net/http"
	"os"
	"strings"
	"text/template"
)

func Consume(body string, db *gorm.DB, firestore *firestore.Client, ctx context.Context) string {
	var response string

	var request Model.EmailRequest
	_ = json.Unmarshal([]byte(body), &request)

	user, pass := getCredentials()

	switch request.Flow {
	case "serviceRequest":
		fmt.Print("serviceRequest")
		SendCalendarRequestEmail(user, pass, request.Company, request.Price, request.Drivers, request.Service, db)
	case "serviceConfirmed":
		fmt.Print("serviceConfirmed")
		SendCalendarConfirmEmail(user, pass, request.Company, request.Service, db)
	case "serviceInvoice":
		fmt.Print("serviceInvoice")
		SendCalendarInvoiceEmail(user, pass, request.Company, request.Price, request.Drivers, request.Service, db)

	default:
		fmt.Print("default")
		response = "EMAIL: Error - Unknown flow"
	}

	response = "EMAIL: Email sent successfully"

	return response
}

func getCredentials() (string, string) {
	emailUser := os.Getenv("EMAIL_USER")
	emailPass := os.Getenv("EMAIL_PASS")
	return emailUser, emailPass
}

func SendCalendarConfirmEmail(user string, password string, company string, service Model.Service, db *gorm.DB) {

	t, _ := template.ParseFiles("email/Template/calendar-confirm-template.html")

	var body bytes.Buffer
	driver := Database.GetDriver(db, service.DriverId)
	client := Database.GetClient(db, service.ClientId)
	//mimeHeaders := "MIME-version: 1.0;\nContent-Type: text/html; charset=\"UTF-8\";\n\n"
	//body.Write([]byte(fmt.Sprintf("Subject: This is a test subject \n%s\n\n", mimeHeaders)))

	t.Execute(&body, struct {
		DriverName      string
		DriverEmail     string
		ClientName      string
		ClientEmail     string
		CompanyName     string
		ServiceDate     string
		ServiceTime     string
		ServiceDatetime string
		Origin          string
	}{
		DriverName:      driver.Name,
		ClientName:      client.Name,
		CompanyName:     company,
		ClientEmail:     client.Email,
		DriverEmail:     driver.Email,
		ServiceDate:     service.ServiceDatetime.Format("01-02-2006"),
		ServiceTime:     service.ServiceDatetime.Format("15:04:05"),
		ServiceDatetime: service.ServiceDatetime.Format("2006-01-02 15:04:05"),
		Origin:          service.Origin,
	})

	result := body.String()

	m := gomail.NewMessage()
	m.SetHeader("From", user)
	m.SetHeader("To", client.Email)
	m.SetAddressHeader("Cc", driver.Email, company)
	m.SetHeader("Subject", company+" - Service Confirmed")
	m.SetBody("text/html", result)
	m.Embed("email/Template/images/CEO_-_Video_Conference.png")
	m.Embed("email/Template/images/facebook2x.png")
	m.Embed("email/Template/images/googleplus2x.png")
	m.Embed("email/Template/images/instagram2x.png")
	m.Embed("email/Template/images/Location_-_P.png")
	m.Embed("email/Template/images/Logo_Octopus.png")
	m.Embed("email/Template/images/Time-p.png")
	m.Embed("email/Template/images/twitter2x.png")
	m.Embed("email/Template/images/User_-_p.png")

	d := gomail.NewDialer("smtp.gmail.com", 587, user, password)

	// Send the email to Bob, Cora and Dan.
	if err := d.DialAndSend(m); err != nil {
		panic(err)
	}

	fmt.Println("Email sent to: " + client.Email + " and " + driver.Email)

}

func SendCalendarRequestEmail(user string, password string, company string, price string, drivers string, service Model.Service, db *gorm.DB) {

	t, _ := template.ParseFiles("email/Template/calendar-request-template.html")

	var body bytes.Buffer
	driver := Database.GetDriver(db, service.DriverId)
	//client := Database.GetClient(db, service.ClientId)
	//mimeHeaders := "MIME-version: 1.0;\nContent-Type: text/html; charset=\"UTF-8\";\n\n"
	//body.Write([]byte(fmt.Sprintf("Subject: This is a test subject \n%s\n\n", mimeHeaders)))

	t.Execute(&body, struct {
		DriverName      string
		DriverEmail     string
		CompanyName     string
		ServiceDate     string
		ServiceTime     string
		ServiceDatetime string
		Origin          string
		Destination     string
		Price           string
		Drivers         string
	}{
		DriverName:      driver.Name,
		CompanyName:     company,
		DriverEmail:     driver.Email,
		ServiceDate:     service.ServiceDatetime.Format("01-02-2006"),
		ServiceTime:     service.ServiceDatetime.Format("15:04:05"),
		ServiceDatetime: service.ServiceDatetime.Format("2006-01-02 15:04:05"),
		Origin:          service.Origin,
		Destination:     service.Destination,
		Price:           price,
		Drivers:         drivers,
	})

	result := body.String()

	m := gomail.NewMessage()
	m.SetHeader("From", user)
	m.SetHeader("To", driver.Email)
	//m.SetAddressHeader("Cc", "<RECIPIENT CC>", "<RECIPIENT CC NAME>")
	m.SetHeader("Subject", company+" - Service Request")
	m.SetBody("text/html", result)
	m.Embed("email/Template/images/CEO_-_Video_Conference.png")
	m.Embed("email/Template/images/facebook2x.png")
	m.Embed("email/Template/images/googleplus2x.png")
	m.Embed("email/Template/images/instagram2x.png")
	m.Embed("email/Template/images/Location_-_P.png")
	m.Embed("email/Template/images/Logo_Octopus.png")
	m.Embed("email/Template/images/Time-p.png")
	m.Embed("email/Template/images/twitter2x.png")
	m.Embed("email/Template/images/User_-_p.png")

	d := gomail.NewDialer("smtp.gmail.com", 587, user, password)

	// Send the email to Bob, Cora and Dan.
	if err := d.DialAndSend(m); err != nil {
		panic(err)
	}

	fmt.Println("Email sent to: " + driver.Email)

}

func SendCalendarInvoiceEmail(user string, password string, company string, price string, drivers string, service Model.Service, db *gorm.DB) {

	t, _ := template.ParseFiles("email/Template/calendar-invoice-template.html")

	err := DownloadFile("email/file.pdf", "http://localhost:8081/legal/pdf")

	if err != nil {
		panic(err)
	}

	var body bytes.Buffer
	driver := Database.GetDriver(db, service.DriverId)
	client := Database.GetClient(db, service.ClientId)
	//mimeHeaders := "MIME-version: 1.0;\nContent-Type: text/html; charset=\"UTF-8\";\n\n"
	//body.Write([]byte(fmt.Sprintf("Subject: This is a test subject \n%s\n\n", mimeHeaders)))

	t.Execute(&body, struct {
		ClientName      string
		CompanyName     string
		ServiceDatetime string
		ServiceName     string
		ServicePrice    string
		TotalPrice      string
		ExtraServices   string
	}{
		ClientName:      client.Name,
		CompanyName:     company,
		ServiceDatetime: service.ServiceDatetime.Format("2006-01-02 15:04:05"),
		ServiceName:     service.Origin + " - " + service.Destination,
		ServicePrice:    "price test",
		TotalPrice:      "total price test",
		ExtraServices:   "<tr>\n                    <td>{{.ServiceName}}</td>\n                    <td>{{.ServicePrice}}</td>\n                </tr>",
	})

	result := body.String()

	m := gomail.NewMessage()
	m.SetHeader("From", user)
	m.SetHeader("To", client.Email)
	m.SetAddressHeader("Cc", driver.Email, company)
	m.SetHeader("Subject", company+" - Service Invoice")
	m.SetBody("text/html", result)
	m.Embed("email/Template/images/CEO_-_Video_Conference.png")
	m.Embed("email/Template/images/facebook2x.png")
	m.Embed("email/Template/images/googleplus2x.png")
	m.Embed("email/Template/images/instagram2x.png")
	m.Embed("email/Template/images/Location_-_P.png")
	m.Embed("email/Template/images/Logo_Octopus.png")
	m.Embed("email/Template/images/Time-p.png")
	m.Embed("email/Template/images/twitter2x.png")
	m.Embed("email/Template/images/User_-_p.png")
	m.Attach("email/file.pdf")

	d := gomail.NewDialer("smtp.gmail.com", 587, user, password)

	// Send the email to Bob, Cora and Dan.
	if err := d.DialAndSend(m); err != nil {
		panic(err)
	}

	fmt.Println("Email sent to: " + client.Email + " and " + driver.Email)

}

func DownloadFile(filepath string, url string) error {

	// Get the data
	jsonStr := strings.NewReader(`{
    "service": {
        "description": "Test descriptionmodified",
        "origin": "BCN Airporttttt",
        "destination": "Girona Airport",
        "serviceId": 4,
        "driverId": 1,
        "extraPrice": 0.0,
        "serviceDatetime": "2021-02-20T00:00:00Z",
        "payedDatetime": "2020-02-25T00:00:00Z",
        "specialNeeds": "none",
        "passengers": 3,
        "basePrice": 12.0,
        "calendarEvent": "calendarURL",
        "clientId": 1,
        "confirmedDatetime": null
    },
    "flow": "invoice"
}`)
	url2 := "http://localhost:8081/legal/pdf"
	method := "GET"

	client := &http.Client{}
	req, err := http.NewRequest(method, url2, jsonStr)
	req.Header.Add("Content-Type", "application/json")

	res, err := client.Do(req)

	defer res.Body.Close()

	// Create the file
	out, err := os.Create(filepath)
	if err != nil {
		return err
	}
	defer out.Close()

	// Write the body to file
	_, err = io.Copy(out, res.Body)

	return err
}
