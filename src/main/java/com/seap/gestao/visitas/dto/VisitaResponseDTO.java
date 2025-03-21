package com.seap.gestao.visitas.dto;

import com.seap.gestao.visitas.model.Visita;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class VisitaResponseDTO extends VisitaDTO {
    private VisitanteDTO visitante;
    private DetentoDTO detento;

    public static VisitaResponseDTO fromEntity(Visita visita) {
        VisitaResponseDTO dto = new VisitaResponseDTO();
        // Copiar dados da superclasse
        dto.setId(visita.getId());
        dto.setVisitanteId(visita.getVisitante().getId());
        dto.setDetentoId(visita.getDetento().getId());
        dto.setDataHora(visita.getDataHora());
        dto.setStatus(visita.getStatus());
        dto.setDataCriacao(visita.getDataCriacao());
        dto.setDataAtualizacao(visita.getDataAtualizacao());
        dto.setObservacao(visita.getObservacao());
        
        // Adicionar dados completos
        dto.setVisitante(VisitanteDTO.fromEntity(visita.getVisitante()));
        dto.setDetento(DetentoDTO.fromEntity(visita.getDetento()));
        
        return dto;
    }
}
