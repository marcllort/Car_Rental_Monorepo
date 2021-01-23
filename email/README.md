# Email
Simple microservice. It receives a request from another microservice to send a message (sometimes also a file) to a client. 
It has the feature to schedule an email, in case we want to send the invoice once the service has been finished. 

The microservice is coded in GoLang.

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