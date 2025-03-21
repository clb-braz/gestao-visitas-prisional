package com.seap.gestao.visitas.dto;

import com.seap.gestao.visitas.model.Visita;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class VisitaDTO {
    private Long id;

    @NotNull(message = "O ID do visitante é obrigatório")
    private Long visitanteId;

    @NotNull(message = "O ID do detento é obrigatório")
    private Long detentoId;

    @NotNull(message = "A data e hora são obrigatórias")
    @Future(message = "A data e hora devem ser no futuro")
    private LocalDateTime dataHora;

    private Visita.StatusVisita status = Visita.StatusVisita.AGENDADA;

    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;

    @Size(max = 500, message = "A observação deve ter no máximo 500 caracteres")
    private String observacao;

    public static VisitaDTO fromEntity(Visita visita) {
        VisitaDTO dto = new VisitaDTO();
        dto.setId(visita.getId());
        dto.setVisitanteId(visita.getVisitante().getId());
        dto.setDetentoId(visita.getDetento().getId());
        dto.setDataHora(visita.getDataHora());
        dto.setStatus(visita.getStatus());
        dto.setDataCriacao(visita.getDataCriacao());
        dto.setDataAtualizacao(visita.getDataAtualizacao());
        dto.setObservacao(visita.getObservacao());
        return dto;
    }
}
