package com.tecsup.repository;

import com.tecsup.model.AuditoriaLog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AuditoriaLogRepository extends JpaRepository<AuditoriaLog, Long>{
}
