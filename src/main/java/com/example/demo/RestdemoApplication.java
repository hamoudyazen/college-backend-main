package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;

import java.io.IOException;
@SpringBootApplication
public class RestdemoApplication {
@Autowired
private EmailSenderService senderService;
	public static void main(String[] args) {
		try {
			FirebaseConfig.init();
		} catch (IOException e) {
			e.printStackTrace();
		}

		SpringApplication.run(RestdemoApplication.class, args);
	}

}
