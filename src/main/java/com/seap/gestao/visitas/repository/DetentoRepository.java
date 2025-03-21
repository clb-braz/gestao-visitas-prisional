package com.seap.gestao.visitas.repository;

import com.seap.gestao.visitas.model.Detento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface DetentoRepository extends JpaRepository<Detento, Long> {
    Optional<Detento> findByMatricula(String matricula);
    boolean existsByMatricula(String matricula);
}
