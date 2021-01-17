package com.nab.trang.product_service.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageProducer {
	
	private static final Logger LOGGER = LogManager.getLogger(MessageProducer.class);
	
	@Autowired
	private RabbitTemplate template;
	
	@Autowired
	private Queue queue;

	public void send(String searchKeyWord) {
		try {
			this.template.convertAndSend(queue.getName(), searchKeyWord);
			LOGGER.info(" [x] Sent '" + searchKeyWord + "'");
		} catch(Exception e) {
			LOGGER.error("Send message failed", e);
		}
	}
}