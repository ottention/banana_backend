package com.ottention.banana;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class BananaApplication {

	public static void main(String[] args) {
		SpringApplication.run(BananaApplication.class, args);
	}

}
