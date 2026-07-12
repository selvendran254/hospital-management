package com.hospital.management.aspect;

import com.hospital.management.domain.entity.AuditLog;
import com.hospital.management.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class AuditLogAspect {

    private final AuditLogRepository auditLogRepository;

    @AfterReturning("execution(* com.hospital.management.service..*.create*(..)) || " +
            "execution(* com.hospital.management.service..*.update*(..)) || " +
            "execution(* com.hospital.management.service..*.delete*(..))")
    public void logChange(JoinPoint joinPoint) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String user = auth != null ? auth.getName() : "system";
        String method = joinPoint.getSignature().getName();
        String className = joinPoint.getSignature().getDeclaringTypeName();
        auditLogRepository.save(AuditLog.builder()
                .action(method.toUpperCase())
                .entityName(className)
                .entityId(null)
                .performedBy(user)
                .details("Method executed: " + method)
                .build());
    }
}
