package com.nab.trang.audit_service.config;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuditServiceConfiguration {

	@Value("${product-service.search-queue}")
	private String queueName;
	
	@Bean
    public Queue searchQueue() {
        return new Queue(queueName, false);
    }
	
}
