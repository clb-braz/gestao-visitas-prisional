package com.seap.gestao.visitas.service;

import com.seap.gestao.visitas.dto.VisitanteDTO;
import com.seap.gestao.visitas.model.Visitante;
import com.seap.gestao.visitas.repository.VisitanteRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VisitanteService {

    private final VisitanteRepository visitanteRepository;

    @Autowired
    public VisitanteService(VisitanteRepository visitanteRepository) {
        this.visitanteRepository = visitanteRepository;
    }

    @Transactional(readOnly = true)
    public List<VisitanteDTO> listarTodos() {
        return visitanteRepository.findAll().stream()
                .map(VisitanteDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public VisitanteDTO buscarPorId(Long id) {
        return visitanteRepository.findById(id)
                .map(VisitanteDTO::fromEntity)
                .orElseThrow(() -> new EntityNotFoundException("Visitante não encontrado com ID: " + id));
    }

    @Transactional(readOnly = true)
    public VisitanteDTO buscarPorCpf(String cpf) {
        return visitanteRepository.findByCpf(cpf)
                .map(VisitanteDTO::fromEntity)
                .orElseThrow(() -> new EntityNotFoundException("Visitante não encontrado com CPF: " + cpf));
    }

    @Transactional(readOnly = true)
    public VisitanteDTO buscarPorRg(String rg) {
        return visitanteRepository.findByRg(rg)
                .map(VisitanteDTO::fromEntity)
                .orElseThrow(() -> new EntityNotFoundException("Visitante não encontrado com RG: " + rg));
    }

    @Transactional
    public VisitanteDTO criar(VisitanteDTO visitanteDTO) {
        validarDocumentos(visitanteDTO);
        
        Visitante visitante = visitanteDTO.toEntity();
        visitante = visitanteRepository.save(visitante);
        return VisitanteDTO.fromEntity(visitante);
    }

    @Transactional
    public VisitanteDTO atualizar(Long id, VisitanteDTO visitanteDTO) {
        Visitante visitanteExistente = visitanteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Visitante não encontrado com ID: " + id));

        validarDocumentosAtualizacao(id, visitanteDTO);

        visitanteExistente.setNome(visitanteDTO.getNome());
        visitanteExistente.setCpf(visitanteDTO.getCpf());
        visitanteExistente.setRg(visitanteDTO.getRg());
        visitanteExistente.setDataNascimento(visitanteDTO.getDataNascimento());
        visitanteExistente.setEmail(visitanteDTO.getEmail());
        visitanteExistente.setTelefone(visitanteDTO.getTelefone());
        visitanteExistente.setEndereco(visitanteDTO.getEndereco());
        visitanteExistente.setStatus(visitanteDTO.getStatus());

        visitanteExistente = visitanteRepository.save(visitanteExistente);
        return VisitanteDTO.fromEntity(visitanteExistente);
    }

    @Transactional
    public void excluir(Long id) {
        if (!visitanteRepository.existsById(id)) {
            throw new EntityNotFoundException("Visitante não encontrado com ID: " + id);
        }
        visitanteRepository.deleteById(id);
    }

    @Transactional
    public VisitanteDTO alterarStatus(Long id, Visitante.StatusVisitante status) {
        Visitante visitante = visitanteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Visitante não encontrado com ID: " + id));
        
        visitante.setStatus(status);
        visitante = visitanteRepository.save(visitante);
        return VisitanteDTO.fromEntity(visitante);
    }

    private void validarDocumentos(VisitanteDTO visitanteDTO) {
        if (visitanteRepository.existsByCpf(visitanteDTO.getCpf())) {
            throw new IllegalArgumentException("Já existe um visitante com o CPF: " + visitanteDTO.getCpf());
        }
        if (visitanteRepository.existsByRg(visitanteDTO.getRg())) {
            throw new IllegalArgumentException("Já existe um visitante com o RG: " + visitanteDTO.getRg());
        }
    }

    private void validarDocumentosAtualizacao(Long id, VisitanteDTO visitanteDTO) {
        visitanteRepository.findByCpf(visitanteDTO.getCpf())
                .filter(v -> !v.getId().equals(id))
                .ifPresent(v -> {
                    throw new IllegalArgumentException("Já existe um visitante com o CPF: " + visitanteDTO.getCpf());
                });

        visitanteRepository.findByRg(visitanteDTO.getRg())
                .filter(v -> !v.getId().equals(id))
                .ifPresent(v -> {
                    throw new IllegalArgumentException("Já existe um visitante com o RG: " + visitanteDTO.getRg());
                });
    }
}
