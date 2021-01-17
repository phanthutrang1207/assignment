package com.nab.trang.audit_service.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.nab.trang.audit_service.model.Audit;

@Repository
public interface AuditRepository extends CrudRepository<Audit, Long>{

}
