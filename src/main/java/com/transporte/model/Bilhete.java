package com.transporte.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.transporte.model.enums.MetodoPagamento;
import com.transporte.model.enums.StatusBilhete;

import jakarta.persistence.*;

@Entity
@Table(name = "bilhete")
public class Bilhete {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdB")
    private Integer id;

    /** HH:mm:ss */
    @Column(name = "hora_compra", nullable = false, length = 8)
    private String horaCompra;

    @Enumerated(EnumType.STRING)
    @Column(name = "metodo_pagamento", nullable = false)
    private MetodoPagamento metodoPagamento;

    @Column(name = "preco_pago", nullable = false, precision = 10, scale = 2)
    private BigDecimal precoPago;

    @Column(name = "numero_assento", nullable = false)
    private Integer numeroAssento;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_bilhete", nullable = false)
    private StatusBilhete statusBilhete = StatusBilhete.ATIVO;

    @Column(name = "data_validade", nullable = false)
    private LocalDate dataValidade;

    @Column(name = "data_compra", nullable = false)
    private LocalDate dataCompra;

    @ManyToOne(optional = false)
    @JoinColumn(name = "IdP")
    private Passageiro passageiro;

    @ManyToOne(optional = false)
    @JoinColumn(name = "IdV")
    private Viagem viagem;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getHoraCompra() { return horaCompra; }
    public void setHoraCompra(String horaCompra) { this.horaCompra = horaCompra; }
    public MetodoPagamento getMetodoPagamento() { return metodoPagamento; }
    public void setMetodoPagamento(MetodoPagamento metodoPagamento) { this.metodoPagamento = metodoPagamento; }
    public BigDecimal getPrecoPago() { return precoPago; }
    public void setPrecoPago(BigDecimal precoPago) { this.precoPago = precoPago; }
    public Integer getNumeroAssento() { return numeroAssento; }
    public void setNumeroAssento(Integer numeroAssento) { this.numeroAssento = numeroAssento; }
    public StatusBilhete getStatusBilhete() { return statusBilhete; }
    public void setStatusBilhete(StatusBilhete statusBilhete) { this.statusBilhete = statusBilhete; }
    public LocalDate getDataValidade() { return dataValidade; }
    public void setDataValidade(LocalDate dataValidade) { this.dataValidade = dataValidade; }
    public LocalDate getDataCompra() { return dataCompra; }
    public void setDataCompra(LocalDate dataCompra) { this.dataCompra = dataCompra; }
    public Passageiro getPassageiro() { return passageiro; }
    public void setPassageiro(Passageiro passageiro) { this.passageiro = passageiro; }
    public Viagem getViagem() { return viagem; }
    public void setViagem(Viagem viagem) { this.viagem = viagem; }
}