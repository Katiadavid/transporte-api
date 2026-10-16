package com.transporte.model;

import com.transporte.model.enums.TipoVeiculo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "modelo_veiculo")
@Getter @Setter @NoArgsConstructor
public class ModeloVeiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdModelo")
    private Integer id;

    @Column(name = "modelo", nullable = false, length = 60)
    private String modelo;

    @Column(name = "marca", nullable = false, length = 60)
    private String marca;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_veiculo", nullable = false)
    private TipoVeiculo tipoVeiculo;

    @Column(name = "capacidade_max", nullable = false)
    private Integer capacidadeMax;
}