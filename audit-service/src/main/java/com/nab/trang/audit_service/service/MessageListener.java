package com.nab.trang.audit_service.service;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nab.trang.audit_service.model.Audit;

@Service
@RabbitListener(queues = "${product-service.search-queue}")
public class MessageListener {
	
	@Autowired
	private AuditService auditService;

    @RabbitHandler
    public void receive(String searchString) {
    	System.out.println(" [x] Received '" + searchString + "'");
    	try {
	    	Audit audit = new Audit();
	    	audit.setSearchString(searchString);
	    	
	    	auditService.saveAudit(audit);
    	} catch (Exception e) {
    		// Ignore exception
    		System.out.println(e);
    	}
    }
}