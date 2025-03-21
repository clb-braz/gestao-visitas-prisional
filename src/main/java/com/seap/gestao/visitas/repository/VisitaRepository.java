package com.seap.gestao.visitas.repository;

import com.seap.gestao.visitas.model.Visita;
import com.seap.gestao.visitas.model.Detento;
import com.seap.gestao.visitas.model.Visitante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VisitaRepository extends JpaRepository<Visita, Long> {
    List<Visita> findByVisitante(Visitante visitante);
    List<Visita> findByDetento(Detento detento);
    List<Visita> findByStatus(Visita.StatusVisita status);
    
    @Query("SELECT v FROM Visita v WHERE v.detento = ?1 AND v.dataHora BETWEEN ?2 AND ?3")
    List<Visita> findByDetentoAndPeriodo(Detento detento, LocalDateTime inicio, LocalDateTime fim);
    
    @Query("SELECT COUNT(v) FROM Visita v WHERE v.detento = ?1 AND v.dataHora BETWEEN ?2 AND ?3 AND v.status IN ('AGENDADA', 'CONFIRMADA')")
    long countVisitasAgendadasNaSemana(Detento detento, LocalDateTime inicio, LocalDateTime fim);
}
