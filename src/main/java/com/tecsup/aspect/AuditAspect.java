package com.tecsup.aspect;

import com.tecsup.model.AuditoriaLog;
import com.tecsup.repository.AuditoriaLogRepository;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import java.sql.Timestamp;
import java.time.Instant;

@Aspect
@Component
public class AuditAspect {
    @Autowired
    private AuditoriaLogRepository auditoriaLogRepository;

    @AfterReturning(
            "execution(* com.tecsup.service.*.guardar(..)) || " +
                    "execution(* com.tecsup.service.*.actualizar(..)) || " +
                    "execution(* com.tecsup.service.*.eliminar(..)) || " +
                    "execution(* com.tecsup.service.*.registrarPedido(..))"
    )
    public void auditar(JoinPoint joinPoint) {

        String usuario = "anonimo";
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            usuario = SecurityContextHolder.getContext().getAuthentication().getName();
        }

        AuditoriaLog registro = new AuditoriaLog();
        registro.setAccion(joinPoint.getSignature().getName().toUpperCase());
        registro.setMetodo(
                joinPoint.getSignature().getDeclaringTypeName() + "." +
                        joinPoint.getSignature().getName()
        );
        registro.setFecha(Timestamp.from(Instant.now()));
        registro.setUsuario(usuario);

        auditoriaLogRepository.save(registro);
    }
}
