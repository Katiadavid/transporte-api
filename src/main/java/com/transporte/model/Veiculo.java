package com.transporte.model;

import java.util.Objects;

import com.transporte.model.enums.StatusVeiculo;

import jakarta.persistence.*;

@Entity
@Table(name = "veiculo")
public class Veiculo {

    @Id
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

    public String getMatricula() { return matricula; }
    public void setMatricula(String matricula) { this.matricula = matricula; }
    public ModeloVeiculo getModelo() { return modelo; }
    public void setModelo(ModeloVeiculo modelo) { this.modelo = modelo; }
    public String getCor() { return cor; }
    public void setCor(String cor) { this.cor = cor; }
    public Integer getAnoFabrico() { return anoFabrico; }
    public void setAnoFabrico(Integer anoFabrico) { this.anoFabrico = anoFabrico; }
    public StatusVeiculo getStatusVeiculo() { return statusVeiculo; }
    public void setStatusVeiculo(StatusVeiculo statusVeiculo) { this.statusVeiculo = statusVeiculo; }
    public Integer getQuilometragem() { return quilometragem; }
    public void setQuilometragem(Integer quilometragem) { this.quilometragem = quilometragem; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Veiculo other)) return false;
        return matricula != null && matricula.equals(other.matricula);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(matricula);
    }
}