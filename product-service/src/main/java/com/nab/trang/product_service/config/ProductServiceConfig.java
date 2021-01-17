package com.nab.trang.product_service.config;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ProductServiceConfig {

	@Value("${product-service.search-queue}")
	private String queueName;
	
	@Bean
    public Queue searchQueue() {
        return new Queue(queueName, false);
    }
	
	@Bean
	public RestTemplate restTemplate() {
	    return new RestTemplate();
	}
}
