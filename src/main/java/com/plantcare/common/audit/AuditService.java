package com.plantcare.common.audit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AuditService {

    private static final Logger log = LoggerFactory.getLogger(AuditService.class);

    private final AuditLogRepository auditLogRepository;

    public AuditService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    @Transactional
    public void logAction(UUID userId, String action, String entityType, String entityId, String oldValue, String newValue, String ipAddress) {
        try {
            AuditLog logEntry = new AuditLog();
            logEntry.setUserId(userId);
            logEntry.setAction(action);
            logEntry.setEntityType(entityType);
            logEntry.setEntityId(entityId);
            logEntry.setOldValue(oldValue);
            logEntry.setNewValue(newValue);
            logEntry.setIpAddress(ipAddress);
            logEntry.setTimestamp(LocalDateTime.now());
            
            auditLogRepository.save(logEntry);
        } catch (Exception e) {
            log.error("Failed to write audit log entry: {}", e.getMessage(), e);
        }
    }
}
