package com.seap.gestao.visitas.dto;

import com.seap.gestao.visitas.model.Detento;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDate;

@Data
public class DetentoDTO {
    private Long id;

    @NotBlank(message = "O nome é obrigatório")
    @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres")
    private String nome;

    @NotBlank(message = "A matrícula é obrigatória")
    private String matricula;

    @NotNull(message = "A data de nascimento é obrigatória")
    @Past(message = "A data de nascimento deve ser no passado")
    private LocalDate dataNascimento;

    @NotNull(message = "O pavilhão é obrigatório")
    private Detento.Pavilhao pavilhao;

    @NotNull(message = "O regime é obrigatório")
    private Detento.RegimePrisional regime;

    private boolean ativo = true;

    public static DetentoDTO fromEntity(Detento detento) {
        DetentoDTO dto = new DetentoDTO();
        dto.setId(detento.getId());
        dto.setNome(detento.getNome());
        dto.setMatricula(detento.getMatricula());
        dto.setDataNascimento(detento.getDataNascimento());
        dto.setPavilhao(detento.getPavilhao());
        dto.setRegime(detento.getRegime());
        dto.setAtivo(detento.isAtivo());
        return dto;
    }

    public Detento toEntity() {
        Detento detento = new Detento();
        detento.setId(this.id);
        detento.setNome(this.nome);
        detento.setMatricula(this.matricula);
        detento.setDataNascimento(this.dataNascimento);
        detento.setPavilhao(this.pavilhao);
        detento.setRegime(this.regime);
        detento.setAtivo(this.ativo);
        return detento;
    }
}
