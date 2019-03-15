package com.daogu.data.ocrservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.retry.annotation.EnableRetry;


@SpringBootApplication
@EnableCircuitBreaker
@EnableRetry
public class OcrServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OcrServiceApplication.class, args);
    }


}
