# Email

Simple microservice. It receives a request from another microservice to send a message (sometimes also a file) to a
client. It has the feature to schedule an email, in case we want to send the invoice once the service has been finished.

The microservice is coded in GoLang.

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

## Firestore

This microservice is the responsible to send the emails to the user. To do so, the credentials of the "sender" are
needed. The credentials (email and password) are encrypted and stored in the user configuration of Firestore.

To retrieve them, a connection to the Firestore project is needed first:

```
func CreateFirestoreConnection(google_creds string) (*firestore.Client, context.Context) {
	ctx := context.Background()
	sa := option.WithCredentialsFile(google_creds)
	app, err := firebase.NewApp(ctx, nil, sa)
	if err != nil {
		log.Fatalln(err)
	}

	client, err := app.Firestore(ctx)
	if err != nil {
		log.Fatalln(err)
	}

	return client, ctx
}
```

It is as simple as passing the JSON credentials file of the project, and we are connected to firestore.

```
func GetDocuments(firestore *firestore.Client, ctx context.Context) {
	iter := firestore.Collection("users").Documents(ctx)
	for {
		doc, err := iter.Next()
		if err == iterator.Done {
			break
		}
		if err != nil {
			log.Fatalf("Failed to iterate: %v", err)
		}
		fmt.Println(doc.Data())
	}
}
```

Then, to retrieve we must determine the collection that we want to retrieve the data from.

## Environment Variables

- URL: Depending on if we are in the development environment or in Kubernetes, the RabbitMQ URL is different. This is
  why it must be set as an environment variable, so depending on where the program is running, it will use the
  development URL (localhost) or the production URL (rabbitmq).

- SECRET_DB: The password of the AWS RDS database.

- CREDS: A json file that contains the user, port, URL and database name of the DB.

- SECRET_FIREBASE: A json file that contains the firebase project configuration and secrets to be able to connect to it.