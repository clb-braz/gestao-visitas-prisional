package com.seap.gestao.visitas.service;

import com.seap.gestao.visitas.dto.VisitaDTO;
import com.seap.gestao.visitas.dto.VisitaResponseDTO;
import com.seap.gestao.visitas.model.Visita;
import com.seap.gestao.visitas.model.Detento;
import com.seap.gestao.visitas.model.Visitante;
import com.seap.gestao.visitas.repository.VisitaRepository;
import com.seap.gestao.visitas.repository.DetentoRepository;
import com.seap.gestao.visitas.repository.VisitanteRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.DayOfWeek;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VisitaService {

    private final VisitaRepository visitaRepository;
    private final DetentoRepository detentoRepository;
    private final VisitanteRepository visitanteRepository;

    @Autowired
    public VisitaService(VisitaRepository visitaRepository,
                        DetentoRepository detentoRepository,
                        VisitanteRepository visitanteRepository) {
        this.visitaRepository = visitaRepository;
        this.detentoRepository = detentoRepository;
        this.visitanteRepository = visitanteRepository;
    }

    @Transactional(readOnly = true)
    public List<VisitaResponseDTO> listarTodas() {
        return visitaRepository.findAll().stream()
                .map(VisitaResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public VisitaResponseDTO buscarPorId(Long id) {
        return visitaRepository.findById(id)
                .map(VisitaResponseDTO::fromEntity)
                .orElseThrow(() -> new EntityNotFoundException("Visita não encontrada com ID: " + id));
    }

    @Transactional
    public VisitaResponseDTO agendar(VisitaDTO visitaDTO) {
        validarAgendamento(visitaDTO);

        Detento detento = detentoRepository.findById(visitaDTO.getDetentoId())
                .orElseThrow(() -> new EntityNotFoundException("Detento não encontrado com ID: " + visitaDTO.getDetentoId()));

        Visitante visitante = visitanteRepository.findById(visitaDTO.getVisitanteId())
                .orElseThrow(() -> new EntityNotFoundException("Visitante não encontrado com ID: " + visitaDTO.getVisitanteId()));

        if (!detento.isAtivo()) {
            throw new IllegalStateException("Detento não está ativo para receber visitas");
        }

        if (visitante.getStatus() != Visitante.StatusVisitante.ATIVO) {
            throw new IllegalStateException("Visitante não está autorizado a realizar visitas");
        }

        Visita visita = new Visita();
        visita.setDetento(detento);
        visita.setVisitante(visitante);
        visita.setDataHora(visitaDTO.getDataHora());
        visita.setObservacao(visitaDTO.getObservacao());
        visita.setStatus(Visita.StatusVisita.AGENDADA);

        visita = visitaRepository.save(visita);
        return VisitaResponseDTO.fromEntity(visita);
    }

    @Transactional
    public VisitaResponseDTO alterarStatus(Long id, Visita.StatusVisita novoStatus) {
        Visita visita = visitaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Visita não encontrada com ID: " + id));

        validarMudancaStatus(visita, novoStatus);
        
        visita.setStatus(novoStatus);
        visita = visitaRepository.save(visita);
        return VisitaResponseDTO.fromEntity(visita);
    }

    private void validarAgendamento(VisitaDTO visitaDTO) {
        LocalDateTime dataHora = visitaDTO.getDataHora();
        LocalDateTime agora = LocalDateTime.now();

        // Validar antecedência mínima de 24h
        if (ChronoUnit.HOURS.between(agora, dataHora) < 24) {
            throw new IllegalArgumentException("A visita deve ser agendada com no mínimo 24h de antecedência");
        }

        // Validar horário comercial (8h às 17h) e dias úteis
        if (dataHora.getHour() < 8 || dataHora.getHour() >= 17 ||
            dataHora.getDayOfWeek() == DayOfWeek.SATURDAY ||
            dataHora.getDayOfWeek() == DayOfWeek.SUNDAY) {
            throw new IllegalArgumentException("As visitas só podem ser agendadas em dias úteis, das 8h às 17h");
        }

        // Validar limite de visitas por semana
        LocalDateTime inicioDaSemana = dataHora.with(DayOfWeek.MONDAY).withHour(0).withMinute(0);
        LocalDateTime fimDaSemana = inicioDaSemana.plusDays(6).withHour(23).withMinute(59);

        long visitasNaSemana = visitaRepository.countVisitasAgendadasNaSemana(
            detentoRepository.getReferenceById(visitaDTO.getDetentoId()),
            inicioDaSemana,
            fimDaSemana
        );

        if (visitasNaSemana >= 2) {
            throw new IllegalArgumentException("O detento já atingiu o limite de 2 visitas por semana");
        }
    }

    private void validarMudancaStatus(Visita visita, Visita.StatusVisita novoStatus) {
        if (visita.getStatus() == novoStatus) {
            return;
        }

        LocalDateTime agora = LocalDateTime.now();

        switch (novoStatus) {
            case CANCELADA:
                if (ChronoUnit.HOURS.between(agora, visita.getDataHora()) < 2) {
                    throw new IllegalStateException("A visita só pode ser cancelada com no mínimo 2h de antecedência");
                }
                break;
            case CONFIRMADA:
                if (visita.getStatus() != Visita.StatusVisita.AGENDADA) {
                    throw new IllegalStateException("Apenas visitas agendadas podem ser confirmadas");
                }
                break;
            case REALIZADA:
                if (visita.getStatus() != Visita.StatusVisita.CONFIRMADA) {
                    throw new IllegalStateException("Apenas visitas confirmadas podem ser marcadas como realizadas");
                }
                break;
            case NAO_REALIZADA:
                if (visita.getStatus() != Visita.StatusVisita.CONFIRMADA) {
                    throw new IllegalStateException("Apenas visitas confirmadas podem ser marcadas como não realizadas");
                }
                break;
            default:
                throw new IllegalArgumentException("Status de visita inválido");
        }
    }

    @Transactional(readOnly = true)
    public List<VisitaResponseDTO> buscarPorDetento(Long detentoId) {
        Detento detento = detentoRepository.findById(detentoId)
                .orElseThrow(() -> new EntityNotFoundException("Detento não encontrado com ID: " + detentoId));
        
        return visitaRepository.findByDetento(detento).stream()
                .map(VisitaResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<VisitaResponseDTO> buscarPorVisitante(Long visitanteId) {
        Visitante visitante = visitanteRepository.findById(visitanteId)
                .orElseThrow(() -> new EntityNotFoundException("Visitante não encontrado com ID: " + visitanteId));
        
        return visitaRepository.findByVisitante(visitante).stream()
                .map(VisitaResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }
}
