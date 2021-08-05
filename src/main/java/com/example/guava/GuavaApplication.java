package com.example.guava;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages="com.example.guava.controller")
public class GuavaApplication {

    public static void main(String[] args) {
        SpringApplication.run(GuavaApplication.class, args);
    }

}