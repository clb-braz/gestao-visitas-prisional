package com.seap.gestao.visitas.service;

import com.seap.gestao.visitas.dto.DetentoDTO;
import com.seap.gestao.visitas.model.Detento;
import com.seap.gestao.visitas.repository.DetentoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DetentoService {

    private final DetentoRepository detentoRepository;

    @Autowired
    public DetentoService(DetentoRepository detentoRepository) {
        this.detentoRepository = detentoRepository;
    }

    @Transactional(readOnly = true)
    public List<DetentoDTO> listarTodos() {
        return detentoRepository.findAll().stream()
                .map(DetentoDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public DetentoDTO buscarPorId(Long id) {
        return detentoRepository.findById(id)
                .map(DetentoDTO::fromEntity)
                .orElseThrow(() -> new EntityNotFoundException("Detento não encontrado com ID: " + id));
    }

    @Transactional(readOnly = true)
    public DetentoDTO buscarPorMatricula(String matricula) {
        return detentoRepository.findByMatricula(matricula)
                .map(DetentoDTO::fromEntity)
                .orElseThrow(() -> new EntityNotFoundException("Detento não encontrado com matrícula: " + matricula));
    }

    @Transactional
    public DetentoDTO criar(DetentoDTO detentoDTO) {
        if (detentoRepository.existsByMatricula(detentoDTO.getMatricula())) {
            throw new IllegalArgumentException("Já existe um detento com a matrícula: " + detentoDTO.getMatricula());
        }
        
        Detento detento = detentoDTO.toEntity();
        detento = detentoRepository.save(detento);
        return DetentoDTO.fromEntity(detento);
    }

    @Transactional
    public DetentoDTO atualizar(Long id, DetentoDTO detentoDTO) {
        Detento detentoExistente = detentoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Detento não encontrado com ID: " + id));

        if (!detentoExistente.getMatricula().equals(detentoDTO.getMatricula()) &&
            detentoRepository.existsByMatricula(detentoDTO.getMatricula())) {
            throw new IllegalArgumentException("Já existe um detento com a matrícula: " + detentoDTO.getMatricula());
        }

        detentoExistente.setNome(detentoDTO.getNome());
        detentoExistente.setMatricula(detentoDTO.getMatricula());
        detentoExistente.setDataNascimento(detentoDTO.getDataNascimento());
        detentoExistente.setPavilhao(detentoDTO.getPavilhao());
        detentoExistente.setRegime(detentoDTO.getRegime());
        detentoExistente.setAtivo(detentoDTO.isAtivo());

        detentoExistente = detentoRepository.save(detentoExistente);
        return DetentoDTO.fromEntity(detentoExistente);
    }

    @Transactional
    public void excluir(Long id) {
        if (!detentoRepository.existsById(id)) {
            throw new EntityNotFoundException("Detento não encontrado com ID: " + id);
        }
        detentoRepository.deleteById(id);
    }

    @Transactional
    public DetentoDTO alterarStatus(Long id, boolean ativo) {
        Detento detento = detentoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Detento não encontrado com ID: " + id));
        
        detento.setAtivo(ativo);
        detento = detentoRepository.save(detento);
        return DetentoDTO.fromEntity(detento);
    }
}
