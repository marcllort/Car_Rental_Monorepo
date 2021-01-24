package com.car.rental.legal

import org.springframework.amqp.rabbit.annotation.RabbitHandler
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.stereotype.Service

@SpringBootApplication
class ConsumerApplication

fun main(args: Array<String>) {
    runApplication<ConsumerApplication>(*args)
}

@Service
@RabbitListener(queues = ["legal-queue"])
class RabbitReceiver {
    @RabbitHandler
    fun receive(name: String): String {
        println("Received: '$name'")
        return "Received: '$name'"
    }
}