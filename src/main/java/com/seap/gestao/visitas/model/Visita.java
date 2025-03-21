package com.seap.gestao.visitas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "visitas")
public class Visita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "visitante_id")
    @NotNull(message = "O visitante é obrigatório")
    private Visitante visitante;

    @ManyToOne
    @JoinColumn(name = "detento_id")
    @NotNull(message = "O detento é obrigatório")
    private Detento detento;

    @NotNull(message = "A data e hora são obrigatórias")
    @Future(message = "A data e hora devem ser no futuro")
    private LocalDateTime dataHora;

    @NotNull(message = "O status é obrigatório")
    @Enumerated(EnumType.STRING)
    private StatusVisita status = StatusVisita.AGENDADA;

    @Column(updatable = false)
    private LocalDateTime dataCriacao = LocalDateTime.now();

    private LocalDateTime dataAtualizacao;

    @Size(max = 500, message = "A observação deve ter no máximo 500 caracteres")
    private String observacao;

    public enum StatusVisita {
        AGENDADA, CONFIRMADA, REALIZADA, CANCELADA, NAO_REALIZADA
    }

    @PreUpdate
    protected void onUpdate() {
        dataAtualizacao = LocalDateTime.now();
    }
}
