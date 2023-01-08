package org.zerock.balloon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class BalloonApplication {

	public static void main(String[] args) {
		SpringApplication.run(BalloonApplication.class, args);
	}

}
