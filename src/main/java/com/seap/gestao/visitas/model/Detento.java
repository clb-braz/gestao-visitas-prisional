package com.seap.gestao.visitas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "detentos")
public class Detento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome é obrigatório")
    @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres")
    private String nome;

    @NotBlank(message = "A matrícula é obrigatória")
    @Column(unique = true)
    private String matricula;

    @NotNull(message = "A data de nascimento é obrigatória")
    @Past(message = "A data de nascimento deve ser no passado")
    private LocalDate dataNascimento;

    @NotNull(message = "O pavilhão é obrigatório")
    @Enumerated(EnumType.STRING)
    private Pavilhao pavilhao;

    @NotNull(message = "O regime é obrigatório")
    @Enumerated(EnumType.STRING)
    private RegimePrisional regime;

    private boolean ativo = true;

    public enum Pavilhao {
        A, B, C, D, E, F
    }

    public enum RegimePrisional {
        FECHADO, SEMI_ABERTO, ABERTO
    }
}
