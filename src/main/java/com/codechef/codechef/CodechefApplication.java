package com.codechef.codechef;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CodechefApplication {

	public static void main(String[] args) {
		SpringApplication.run(CodechefApplication.class, args);
	}

}
