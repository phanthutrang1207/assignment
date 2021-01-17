package com.nab.trang.product_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
@EnableRedisRepositories
@EnableEurekaClient
public class ProductServiceApplication {	
	public static void main(String[] args) {
		SpringApplication.run(ProductServiceApplication.class, args);
	}
}
