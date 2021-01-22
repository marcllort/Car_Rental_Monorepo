package com.car.rental.legal

import org.springframework.amqp.rabbit.annotation.*
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.stereotype.Service

@SpringBootApplication
class ConsumerApplication

fun main(args: Array<String>) {
    runApplication<ConsumerApplication>(*args)
}

@Service
@RabbitListener(
    bindings = [QueueBinding(
        value = Queue("legal"),
        exchange = Exchange("amqp.topic"),
        key = ["foo"]
    )]
)
class RabbitReceiver {
    @RabbitHandler
    fun receive(name: String) {
        println("Received: '$name'")
    }
}