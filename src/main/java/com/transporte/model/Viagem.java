package com.transporte.model;

import java.time.LocalDate;

import com.transporte.model.enums.StatusViagem;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "viagem")
@Getter @Setter @NoArgsConstructor
public class Viagem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdV")
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_viagem", nullable = false)
    private StatusViagem statusViagem = StatusViagem.AGENDADA;

    @Column(name = "data_partida", nullable = false)
    private LocalDate dataPartida;

    /** HH:mm */
    @Column(name = "hora_partida", nullable = false, length = 5)
    private String horaPartida;

    /** HH:mm */
    @Column(name = "hora_chegada", nullable = false, length = 5)
    private String horaChegada;

    @Column(name = "observacoes")
    private String observacoes;

    @ManyToOne(optional = false)
    @JoinColumn(name = "IdR")
    private Rota rota;

    @ManyToOne(optional = false)
    @JoinColumn(name = "matricula")
    private Veiculo veiculo;
}