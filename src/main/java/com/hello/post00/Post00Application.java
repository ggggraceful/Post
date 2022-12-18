package com.hello.post00;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class Post00Application {

	public static void main(String[] args) {

		SpringApplication.run(Post00Application.class, args);
	}

}
