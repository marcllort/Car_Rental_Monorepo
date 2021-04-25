# Calendar

The microservice receives service requests. Then, it creates a new calendar event, which will be accepted/rejected by
the company (in case it is rejected, an email will be sent to the client). It also suggests which driver/car is free to
do that “transfer”.

## Golang Project Setup

Using the following commands, go generates Go modules, which facilitates the download of the different
packages/dependencies of the project.

```
go mod init calendar    // Creates the file where dependencies are saved
go mod tidy             // Tidies and downloads the dependencies
```

## RabbitMQ

This microservice, as all the other ones, is using RabbitMQ to communicate between themselves.

RabbitMQ is an open-source general message broker, which supports protocols including MQTT, AMQP and STOMP. It can
handle high-throughput use cases, such as online payment processing. It can handle background jobs or act as a message
broker between microservices.

The RabbitMQ URL is dependent on the environment variable that the microservice receives, as if we are locally
the `localhost` will be used, but if it is running on Docker, the `rabbitmq` URL (of the Kubernetes cluster) will be
used instead.

Then, the only configuration needed is to define which queue it will connect to (in this case `email-queue`) to receive
the requests and publish the responses.

A function can be defined to be run once a message is received, that returns the response to send back.

## Database

To connect to the database an ORM(Object-relational mapping) is used, to facilitate the creation and retrieval of DB
data. It is called GORM, the most popular ORM for Golang.

```
func CreateConnection(creds, dbpass string) *gorm.DB {
	dbURL := Utils.ReadCredentials(creds, dbpass)

	db, err := gorm.Open(mysql.Open(dbURL), &gorm.Config{})
	if err != nil {
		panic("failed to connect database")
	}
	fmt.Println("DB is connected!")

	return db
}
```

To connect to the DB, this function is used. The ReadCredentials is generating the URL with user and password, and once
we have the url, it is just a matter of passing it to the GORM.Open function, to do the connection to the DB.

```
func GetAllServices(db *gorm.DB) []Model.ServiceView {
	var services []Model.ServiceView

	db.Table("service_view").Find(&services)
	printServices(services)

	return services
}
```

This is a simple example of how the retrieval of all the Services would be stored in the `services` variable.

## Environment Variables

- URL: Depending on if we are in the development environment or in Kubernetes, the RabbitMQ URL is different. This is
  why it must be set as an environment variable, so depending on where the program is running, it will use the
  development URL (localhost) or the production URL (rabbitmq).

- SECRET_DB: The password of the AWS RDS database.

- CREDS: A json file that contains the user, port, URL and database name of the DB.

- CREDS_CALENDAR: A json file that contains Google Calendar API credentials.

- CREDS_FIRESTORE: A json file that contains Firebase API credentials.

## Flows

All the flows (but `newService`) require access to the `calendarClient`, which is the connection to the Google Calendar
API. In conjunction with the calendarClient, there is always the slice of `excludeEmails`, to avoid looking at Birthday
or Vacation calendars.

It is important to note is that a request of type `Model.CalendarRequest` is always used to select the flow and also has
the data later used by the specific flow. In some cases only some fields are needed, instead of all of them. These
needed fields are specified in the following section:

### Get Month Service

- **Required data**: The request contains the startTime. The endTime is calculated adding 1 month of duration to the
  startTime.
- **Functionality**: Returns the services in the given month. Improvement possible would be to be able to filter per
  driver.
- **Involved services**: Calendar API
- **Response**: JSON Array of calendar events

### Create New Service

- **Required data**: The request contains all the service data but the confirmedDatetime.
- **Functionality**: Create a new unconfirmed (not in calendar) service. Basically a request for service, that the
  company must accept/deny.
- **Involved services**: Database
- **Response**: Success/Error message

### Confirm Service

- **Required data**: The request contains all the service data but the confirmedDatetime.
- **Functionality**: Once a service has been confirmed, a confirmedDatetime will be added to the Database table, and a
  Calendar event will be created, which will send invitations to the drivers, add the description, destination...
- **Involved services**: Database and Calendar API
- **Response**: Success/Error message

### Update Service

- **Required data**: The request contains all the service data but the confirmedDatetime.
- **Functionality**: Updates the service that has been sent. In case it has a confirmedDatetime (which means it already
  has a calendar event), will also update the calendar event.
- **Involved services**: Database and Calendar API
- **Response**: Success/Error message

### Free drivers

- **Required data**: The request contains the startTime. The endTime is calculated adding 1:30h of duration to the
  startTime. To be improved, by making this parametrized instead of hardcoded.
- **Functionality**: Returns the drivers that are free between the specified timeframe.
- **Involved services**: Calendar API
- **Response**: Array of drivers emails.

### Driver Setup

- **Required data**: No data required. As an improvement, a slice of extra excludedEmails will be added.
- **Functionality**: Retrieves all the "shared with you" calendars (ignoring the ones in the excludedEmails array) and
  creates a driverUser in the DB.
- **Involved services**: Database and Calendar API
- **Response**: Success/Error message

### Get event by Id

- **Required data**: The event Id is needed.
- **Functionality**: Retrieves the event with the specified ID from your database, with all the information related to
  it.
- **Involved services**: Database
- **Response**: Event information

### Summary

- **Required data**: Company name.
- **Functionality**: Calculates the amount of money that has been generated per month, and in a year.
- **Involved services**: Database
- **Response**: Array of revenue per month, actual month income and annual income.