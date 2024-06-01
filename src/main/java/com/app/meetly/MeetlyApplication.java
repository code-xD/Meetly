package com.app.meetly;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class MeetlyApplication {

    public static void main(String[] args) {
        SpringApplication.run(MeetlyApplication.class, args);
    }

}
