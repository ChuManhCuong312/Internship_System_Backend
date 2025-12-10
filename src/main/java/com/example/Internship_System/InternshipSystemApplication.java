package com.example.Internship_System;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class InternshipSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(InternshipSystemApplication.class, args);
	}

}
