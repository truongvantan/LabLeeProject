package com.lablee.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan({"com.lablee.common.entity", "com.lablee.client.repository"})
public class LabLeeClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(LabLeeClientApplication.class, args);
	}

}
