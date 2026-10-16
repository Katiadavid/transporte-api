package com.transporte.model;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import com.transporte.model.enums.DiaSemana;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "rota")
@Getter @Setter @NoArgsConstructor
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
}