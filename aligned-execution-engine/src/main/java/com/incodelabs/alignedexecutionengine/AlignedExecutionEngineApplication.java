package com.incodelabs.alignedexecutionengine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AlignedExecutionEngineApplication {

    public static void main(String[] args) {
        SpringApplication.run(AlignedExecutionEngineApplication.class, args);
    }

}
