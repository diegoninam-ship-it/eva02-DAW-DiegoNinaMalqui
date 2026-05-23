package com.tecsup.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {
    private static final Logger log = LoggerFactory.getLogger(LoggingAspect.class);

    @Before("execution(* com.tecsup.service.*.*(..))")
    public void logAntes(JoinPoint joinPoint) {
        log.info("▶ Iniciando método: {}.{}",
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName());
    }

    @AfterReturning("execution(* com.tecsup.service.*.*(..))")
    public void logDespues(JoinPoint joinPoint) {
        log.info("✅ Método completado exitosamente: {}",
                joinPoint.getSignature().getName());
    }
}
