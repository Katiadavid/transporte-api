package com.transporte.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "mercadoria")
@Getter @Setter @NoArgsConstructor
public class Mercadoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdMercadoria")
    private Integer id;

    @Column(name = "peso", nullable = false)
    private Double peso;

    @Column(name = "tipo", nullable = false, length = 60)
    private String tipo;

    @ManyToOne(optional = false)
    @JoinColumn(name = "IdP")
    private Passageiro passageiro;
}