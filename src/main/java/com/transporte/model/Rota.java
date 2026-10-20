package com.transporte.model;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import com.transporte.model.enums.DiaSemana;

import jakarta.persistence.*;

@Entity
@Table(name = "rota")
public class Rota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdR")
    private Integer id;

    @Column(name = "origem", nullable = false, length = 80)
    private String origem;

    @Column(name = "destino", nullable = false, length = 80)
    private String destino;

    /** minutos */
    @Column(name = "duracao_estimada", nullable = false)
    private Integer duracaoEstimada;

    @Column(name = "preco_base", nullable = false, precision = 10, scale = 2)
    private BigDecimal precoBase;

    @Column(name = "distancia_km", nullable = false)
    private Double distanciaKm;

    @ElementCollection
    @CollectionTable(name = "rota_dias_operacao", joinColumns = @JoinColumn(name = "IdR"))
    @Enumerated(EnumType.STRING)
    @Column(name = "dia_operacao")
    private Set<DiaSemana> diasOperacao = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "rota_motorista",
            joinColumns = @JoinColumn(name = "IdR"),
            inverseJoinColumns = @JoinColumn(name = "IdM"))
    private Set<Motorista> motoristas = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "rota_veiculo",
            joinColumns = @JoinColumn(name = "IdR"),
            inverseJoinColumns = @JoinColumn(name = "matricula"))
    private Set<Veiculo> veiculos = new HashSet<>();

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getOrigem() { return origem; }
    public void setOrigem(String origem) { this.origem = origem; }
    public String getDestino() { return destino; }
    public void setDestino(String destino) { this.destino = destino; }
    public Integer getDuracaoEstimada() { return duracaoEstimada; }
    public void setDuracaoEstimada(Integer duracaoEstimada) { this.duracaoEstimada = duracaoEstimada; }
    public BigDecimal getPrecoBase() { return precoBase; }
    public void setPrecoBase(BigDecimal precoBase) { this.precoBase = precoBase; }
    public Double getDistanciaKm() { return distanciaKm; }
    public void setDistanciaKm(Double distanciaKm) { this.distanciaKm = distanciaKm; }
    public Set<DiaSemana> getDiasOperacao() { return diasOperacao; }
    public void setDiasOperacao(Set<DiaSemana> diasOperacao) { this.diasOperacao = diasOperacao; }
    public Set<Motorista> getMotoristas() { return motoristas; }
    public void setMotoristas(Set<Motorista> motoristas) { this.motoristas = motoristas; }
    public Set<Veiculo> getVeiculos() { return veiculos; }
    public void setVeiculos(Set<Veiculo> veiculos) { this.veiculos = veiculos; }
}