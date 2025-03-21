package com.seap.gestao.visitas.dto;

import com.seap.gestao.visitas.model.Visitante;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDate;

@Data
public class VisitanteDTO {
    private Long id;

    @NotBlank(message = "O nome é obrigatório")
    @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres")
    private String nome;

    @NotBlank(message = "O CPF é obrigatório")
    @Pattern(regexp = "\\d{11}", message = "CPF deve conter 11 dígitos")
    private String cpf;

    @NotBlank(message = "O RG é obrigatório")
    private String rg;

    @NotNull(message = "A data de nascimento é obrigatória")
    @Past(message = "A data de nascimento deve ser no passado")
    private LocalDate dataNascimento;

    @Email(message = "Email deve ser válido")
    private String email;

    @Pattern(regexp = "\\d{10,11}", message = "Telefone deve ter 10 ou 11 dígitos")
    private String telefone;

    @NotBlank(message = "O endereço é obrigatório")
    @Size(max = 200, message = "O endereço deve ter no máximo 200 caracteres")
    private String endereco;

    private Visitante.StatusVisitante status = Visitante.StatusVisitante.ATIVO;

    public static VisitanteDTO fromEntity(Visitante visitante) {
        VisitanteDTO dto = new VisitanteDTO();
        dto.setId(visitante.getId());
        dto.setNome(visitante.getNome());
        dto.setCpf(visitante.getCpf());
        dto.setRg(visitante.getRg());
        dto.setDataNascimento(visitante.getDataNascimento());
        dto.setEmail(visitante.getEmail());
        dto.setTelefone(visitante.getTelefone());
        dto.setEndereco(visitante.getEndereco());
        dto.setStatus(visitante.getStatus());
        return dto;
    }

    public Visitante toEntity() {
        Visitante visitante = new Visitante();
        visitante.setId(this.id);
        visitante.setNome(this.nome);
        visitante.setCpf(this.cpf);
        visitante.setRg(this.rg);
        visitante.setDataNascimento(this.dataNascimento);
        visitante.setEmail(this.email);
        visitante.setTelefone(this.telefone);
        visitante.setEndereco(this.endereco);
        visitante.setStatus(this.status);
        return visitante;
    }
}
