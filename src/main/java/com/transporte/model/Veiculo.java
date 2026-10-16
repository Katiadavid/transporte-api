package com.transporte.model;

import com.transporte.model.enums.StatusVeiculo;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "veiculo")
@Getter @Setter @NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Veiculo {

    @Id
    @EqualsAndHashCode.Include
    @Column(name = "matricula", length = 15)
    private String matricula;

    @ManyToOne(optional = false)
    @JoinColumn(name = "IdModelo")
    private ModeloVeiculo modelo;

    @Column(name = "cor", nullable = false, length = 30)
    private String cor;

    @Column(name = "ano_fabrico", nullable = false)
    private Integer anoFabrico;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_veiculo", nullable = false)
    private StatusVeiculo statusVeiculo = StatusVeiculo.ATIVO;

    @Column(name = "quilometragem", nullable = false)
    private Integer quilometragem = 0;
}