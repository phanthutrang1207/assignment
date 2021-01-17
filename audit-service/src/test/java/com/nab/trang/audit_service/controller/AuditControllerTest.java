package com.nab.trang.audit_service.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.nab.trang.audit_service.model.Audit;
import com.nab.trang.audit_service.service.AuditService;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@EnableAutoConfiguration(exclude = { DataSourceAutoConfiguration.class })
public class AuditControllerTest {

    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private AuditService auditService;
    
    @Test
    public void testgetSearchAuditSuccessfulWithEmptyList() throws Exception {
    	when(auditService.getAudits()).thenReturn(Collections.emptyList());
    	
    	mockMvc.perform(MockMvcRequestBuilders
    		      .get("/audits")
    		      .accept(MediaType.APPLICATION_JSON))
    		      .andExpect(status().isOk())
    		      .andExpect(jsonPath("$", is(Collections.emptyList())));
    }

    @Test
    public void testgetSearchAuditSuccessfulWithNonEmptyList() throws Exception {
    	Audit audit = new Audit(UUID.randomUUID().toString(), "iphone", LocalDateTime.now());
		List<Audit> audits = Arrays.asList(audit);
    	when(auditService.getAudits()).thenReturn(audits);
    	
    	mockMvc.perform(MockMvcRequestBuilders
    		      .get("/audits")
    		      .accept(MediaType.APPLICATION_JSON))
    		      .andExpect(status().isOk())
    		      .andExpect(jsonPath("$[0].searchString", is(audit.getSearchString())))
    		      .andExpect(jsonPath("$[0].id", is(audit.getId())));
    }
}
