package com.transporte.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.transporte.model.enums.MetodoPagamento;
import com.transporte.model.enums.StatusBilhete;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "bilhete")
@Getter @Setter @NoArgsConstructor
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
}
