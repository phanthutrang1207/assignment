package com.nab.trang.audit_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableEurekaClient
@EnableMongoAuditing
public class AuditServiceApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(AuditServiceApplication.class, args);
	}

}
