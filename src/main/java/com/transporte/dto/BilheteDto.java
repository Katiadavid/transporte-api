package com.transporte.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.transporte.model.Bilhete;
import com.transporte.model.enums.MetodoPagamento;
import com.transporte.model.enums.StatusBilhete;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public final class BilheteDto {
    private BilheteDto() {}

    public record Request(
            @NotNull Integer idP,
            @NotNull Integer idV,
            @NotNull @Positive Integer numeroAssento,
            @NotNull MetodoPagamento metodoPagamento) {}

    public record PassageiroResumo(Integer id, String nome, String nuit) {}

    public record ViagemResumo(Integer id, String origem, String destino, LocalDate dataPartida, String horaPartida) {}

    public record Response(Integer id, String horaCompra, MetodoPagamento metodoPagamento, BigDecimal precoPago,
                           Integer numeroAssento, StatusBilhete statusBilhete, LocalDate dataValidade,
                           LocalDate dataCompra, PassageiroResumo passageiro, ViagemResumo viagem) {
        public static Response from(Bilhete b) {
            var p = b.getPassageiro();
            var v = b.getViagem();
            return new Response(b.getId(), b.getHoraCompra(), b.getMetodoPagamento(), b.getPrecoPago(),
                    b.getNumeroAssento(), b.getStatusBilhete(), b.getDataValidade(), b.getDataCompra(),
                    new PassageiroResumo(p.getId(), p.getNome(), p.getNuit()),
                    new ViagemResumo(v.getId(), v.getRota().getOrigem(), v.getRota().getDestino(),
                            v.getDataPartida(), v.getHoraPartida()));
        }
    }
}