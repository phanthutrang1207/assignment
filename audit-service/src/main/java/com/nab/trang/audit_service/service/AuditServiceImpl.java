package com.nab.trang.audit_service.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nab.trang.audit_service.model.Audit;
import com.nab.trang.audit_service.repository.AuditRepository;

@Service
public class AuditServiceImpl implements AuditService {

	@Autowired
	AuditRepository auditReposity;
	
	
	
	@Override
	public Audit saveAudit(Audit audit) {
		return auditReposity.save(audit);
	}
	
	@Override
	public List<Audit> getAudits() {
		return (List<Audit>) auditReposity.findAll();
	}

}
