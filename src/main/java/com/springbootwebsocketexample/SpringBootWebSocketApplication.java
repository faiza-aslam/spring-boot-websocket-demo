package com.springbootwebsocketexample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SpringBootWebSocketApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootWebSocketApplication.class, args);
	}
}
