package com.transporte.model;

import jakarta.persistence.*;

@Entity
@Table(name = "mercadoria")
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

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Double getPeso() { return peso; }
    public void setPeso(Double peso) { this.peso = peso; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public Passageiro getPassageiro() { return passageiro; }
    public void setPassageiro(Passageiro passageiro) { this.passageiro = passageiro; }
}