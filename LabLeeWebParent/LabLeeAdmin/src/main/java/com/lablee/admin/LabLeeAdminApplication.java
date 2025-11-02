package com.lablee.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan({"com.lablee.common.entity", "com.lablee.admin.user"})
public class LabLeeAdminApplication {

	public static void main(String[] args) {
		SpringApplication.run(LabLeeAdminApplication.class, args);
	}

}
