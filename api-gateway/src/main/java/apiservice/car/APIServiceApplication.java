package apiservice.car;

import apiservice.car.handler.RabbitMQDirectConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class APIServiceApplication {

    @Autowired
    private RabbitMQDirectConfig config;

    public static void main(String[] args) {
        SpringApplication.run(APIServiceApplication.class, args);
    }

}
