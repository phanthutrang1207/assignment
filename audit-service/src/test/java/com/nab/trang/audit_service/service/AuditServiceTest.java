package com.nab.trang.audit_service.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import com.nab.trang.audit_service.model.Audit;
import com.nab.trang.audit_service.repository.AuditRepository;

@RunWith(SpringRunner.class)
public class AuditServiceTest {
	
	@TestConfiguration
	static class AuditServiceTestConfiguration {
		
		@Bean
		public AuditService auditService() {
			return new AuditServiceImpl();
		}
	}
	
	@Autowired
	private AuditService auditService;
	
	@MockBean
	private AuditRepository auditRepository;
	
	@Test
	public void testSaveAuditSuccessful() {
		Audit audit = new Audit(UUID.randomUUID().toString(), "iphone", LocalDateTime.now());
		
		when(auditRepository.save(audit)).thenReturn(audit);
		
		Audit savedAudit = auditService.saveAudit(audit);
		
		verify(auditRepository, times(1)).save(audit);
		assertEquals(audit, savedAudit);
	}
	
	@Test
	public void testGetAuditsSuccessful() {
		Audit audit = new Audit(UUID.randomUUID().toString(), "iphone", LocalDateTime.now());
		List<Audit> audits = Arrays.asList(audit);
		when(auditRepository.findAll()).thenReturn(audits);
		
		List<Audit> result = auditService.getAudits();
		
		verify(auditRepository, times(1)).findAll();
		assertFalse(result.isEmpty());
		assertEquals(audits.get(0), result.get(0));
	}

}
