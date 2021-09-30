package com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;

@Configuration
@EnableAutoConfiguration(exclude={MongoAutoConfiguration.class})
@ComponentScan
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }


    public void onApplicationEvent(ContextRefreshedEvent event) {

        System.out.println("THIS RUNS WITH SPRING STARTS UP!");

        //
        //  create our data!

    }

}
