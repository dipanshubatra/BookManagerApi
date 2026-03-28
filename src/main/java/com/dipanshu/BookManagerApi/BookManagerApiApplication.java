package com.dipanshu.BookManagerApi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BookManagerApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookManagerApiApplication.class, args);
		Logger logger = LoggerFactory.getLogger(BookManagerApiApplication.class);

		logger.info("APP STARTED TEST LOG");
	}
}
