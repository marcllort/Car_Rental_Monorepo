package RabbitMQ

import (
	"cloud.google.com/go/firestore"
	"context"
	"github.com/streadway/amqp"
	"gorm.io/gorm"
	"log"
	"os"
)

type RabbitMQConnection struct {
}

func failOnError(err error, msg string) {
	if err != nil {
		log.Fatalf("%s: %s", msg, err)
	}
}

func Connect(db *gorm.DB, firestore *firestore.Client, ctx context.Context) {
	host := os.Getenv("URL")
	conn, err := amqp.Dial(host)
	failOnError(err, "Failed to connect to RabbitMQ")
	defer conn.Close()

	ch, err := conn.Channel()
	failOnError(err, "Failed to open a channel")
	defer ch.Close()

	msgs, err := ch.Consume(
		"email-queue", // queue
		"",            // consumer
		true,          // auto-ack
		false,         // exclusive
		false,         // no-local
		false,         // no-wait
		nil,           // args
	)
	failOnError(err, "Failed to register a consumer")

	forever := make(chan bool)

	go func() {
		for d := range msgs {
			response := Consume(string(d.Body), db, firestore, ctx)
			publishMessage(err, ch, d, response)
		}
	}()

	log.Printf(" [*] Waiting for messages. To exit press CTRL+C")
	<-forever
}

func publishMessage(err error, ch *amqp.Channel, d amqp.Delivery, response string) {
	err = ch.Publish(
		"",        // exchange
		d.ReplyTo, // routing key
		false,     // mandatory
		false,     // immediate
		amqp.Publishing{
			ContentType:   "text/plain",
			CorrelationId: d.CorrelationId,
			Body:          []byte(response),
		})
	failOnError(err, "Failed to publish a message")
}
