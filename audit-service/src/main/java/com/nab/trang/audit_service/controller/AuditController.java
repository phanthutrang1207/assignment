package com.nab.trang.audit_service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nab.trang.audit_service.model.Audit;
import com.nab.trang.audit_service.service.AuditService;


@RestController
@RequestMapping("/audits")
public class AuditController {
	
	@Autowired
	private AuditService auditService;
	
	@GetMapping
	public ResponseEntity<List<Audit>> getSearchAudit() {
		List<Audit> orders = auditService.getAudits();
		return ResponseEntity.ok(orders);
	}

}
