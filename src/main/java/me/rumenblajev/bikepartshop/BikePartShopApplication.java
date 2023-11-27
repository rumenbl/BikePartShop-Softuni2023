package me.rumenblajev.bikepartshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BikePartShopApplication {

	public static void main(String[] args) {
		SpringApplication.run(BikePartShopApplication.class, args);
	}

}
