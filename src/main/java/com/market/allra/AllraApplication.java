package com.market.allra;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class AllraApplication {

	public static void main(String[] args) {
		SpringApplication.run(AllraApplication.class, args);
	}

}
