package com.car.rental.legal

import com.car.rental.legal.handler.Consumer
import org.springframework.amqp.rabbit.annotation.RabbitHandler
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.beans.factory.annotation.Autowired
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
class RabbitReceiver(@Autowired var consumer: Consumer) {
    @RabbitHandler
    fun receive(request: String): String {
        println("Received: '$request'")
        var response = consumer.consume(request)

        return response
    }
}

