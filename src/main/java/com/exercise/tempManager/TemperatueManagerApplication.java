package com.exercise.tempManager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class TemperatueManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(TemperatueManagerApplication.class, args);
	}

}
