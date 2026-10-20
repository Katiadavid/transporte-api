package com.transporte.model;

import com.transporte.model.enums.TipoVeiculo;

import jakarta.persistence.*;

@Entity
@Table(name = "modelo_veiculo")
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

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }
    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }
    public TipoVeiculo getTipoVeiculo() { return tipoVeiculo; }
    public void setTipoVeiculo(TipoVeiculo tipoVeiculo) { this.tipoVeiculo = tipoVeiculo; }
    public Integer getCapacidadeMax() { return capacidadeMax; }
    public void setCapacidadeMax(Integer capacidadeMax) { this.capacidadeMax = capacidadeMax; }
}