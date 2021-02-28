package RabbitMQ

import (
	"bytes"
	"cloud.google.com/go/firestore"
	"context"
	"email/Model"
	"encoding/json"
	"fmt"
	"gopkg.in/gomail.v2"
	"gorm.io/gorm"
	"os"
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
	case "serviceConfirmed":
		fmt.Print("serviceConfirmed")
		SendCalendarEmail(user, pass, request.Service.Destination)
	case "serviceInvoice":
		fmt.Print("serviceInvoice")

	default:
		fmt.Print("default")
		response = "EMAIL: Error - Unknown flow"
	}

	response = "EMAIL: Email sent successfully"

	return response
}

func getCredentials() (string, string) {
	emailUser := os.Getenv("SECRET_DB")
	emailPass := os.Getenv("SECRET_DB")
	return emailUser, emailPass
}

func SendCalendarEmail(user string, password string, destination string) {

	t, _ := template.ParseFiles("email/Template/calendar-template.html")

	var body bytes.Buffer

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
		DriverName:      "Chope",
		ClientName:      "Marc",
		CompanyName:     "Pressicar",
		ClientEmail:     "testemailclient",
		DriverEmail:     "testemaildriver",
		ServiceDate:     "12/02/1998",
		ServiceTime:     "23:00",
		ServiceDatetime: "23:00 12/02/1998",
		Origin:          "Sagrada Familia",
	})

	result := body.String()

	m := gomail.NewMessage()
	m.SetHeader("From", user)
	m.SetHeader("To", destination)
	//m.SetAddressHeader("Cc", "<RECIPIENT CC>", "<RECIPIENT CC NAME>")
	m.SetHeader("Subject", "golang test")
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

	fmt.Println("Email Sent!")

}
