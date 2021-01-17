package com.nab.trang.audit_service.service;

import java.util.List;

import com.nab.trang.audit_service.model.Audit;

public interface AuditService {
	
	public Audit saveAudit(Audit audit);
	public List<Audit> getAudits();

}
