package com.seap.gestao.visitas.repository;

import com.seap.gestao.visitas.model.Visitante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface VisitanteRepository extends JpaRepository<Visitante, Long> {
    Optional<Visitante> findByCpf(String cpf);
    Optional<Visitante> findByRg(String rg);
    boolean existsByCpf(String cpf);
    boolean existsByRg(String rg);
}
