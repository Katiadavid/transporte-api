package com.transporte.model;

import java.time.LocalDate;

import com.transporte.model.enums.StatusViagem;

import jakarta.persistence.*;

@Entity
@Table(name = "viagem")
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

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public StatusViagem getStatusViagem() { return statusViagem; }
    public void setStatusViagem(StatusViagem statusViagem) { this.statusViagem = statusViagem; }
    public LocalDate getDataPartida() { return dataPartida; }
    public void setDataPartida(LocalDate dataPartida) { this.dataPartida = dataPartida; }
    public String getHoraPartida() { return horaPartida; }
    public void setHoraPartida(String horaPartida) { this.horaPartida = horaPartida; }
    public String getHoraChegada() { return horaChegada; }
    public void setHoraChegada(String horaChegada) { this.horaChegada = horaChegada; }
    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }
    public Rota getRota() { return rota; }
    public void setRota(Rota rota) { this.rota = rota; }
    public Veiculo getVeiculo() { return veiculo; }
    public void setVeiculo(Veiculo veiculo) { this.veiculo = veiculo; }
}