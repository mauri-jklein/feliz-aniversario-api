package com.felizaniversarioapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FelizAniversarioApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(FelizAniversarioApiApplication.class, args);
    }

}
