# Calendar
The microservice receives service requests.
Then, it creates a new calendar event, which will be accepted/rejected by the company (in case it is rejected, an email will be sent to the client).
It also suggests which driver/car is free to do that “transfer”.

## Golang Project Setup
Using the following commands, go generates Go modules, which facilitates the download of the different packages/dependencies of the project.

```
go mod init calendar    // Creates the file where dependencies are saved
go mod tidy             // Tidies and downloads the dependencies
```

## Environment Variables
Depending on if we are in the development environment or in Kubernetes, the RabbitMQ URL is different.

This is why it must be set as an environment variable, so depending on where the program is running,
it will use the development URL (localhost) or the production URL (rabbitmq).