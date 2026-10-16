package com.transporte.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import com.transporte.model.Rota;
import com.transporte.model.enums.DiaSemana;

import jakarta.validation.constraints.*;

public final class RotaDto {
    private RotaDto() {}

    public record Request(
            @NotBlank @Size(min = 2, max = 80) String origem,
            @NotBlank @Size(min = 2, max = 80) String destino,
            @NotNull @Positive Integer duracaoEstimada,
            @NotNull @Positive BigDecimal precoBase,
            @NotNull @Positive Double distanciaKm,
            @NotEmpty Set<DiaSemana> diasOperacao) {}

    public record DiaRequest(@NotNull DiaSemana dia) {}

    public record Response(Integer id, String origem, String destino, Integer duracaoEstimada,
                           BigDecimal precoBase, Double distanciaKm, List<DiaSemana> diasOperacao) {
        public static Response from(Rota r) {
            return new Response(r.getId(), r.getOrigem(), r.getDestino(), r.getDuracaoEstimada(),
                    r.getPrecoBase(), r.getDistanciaKm(), r.getDiasOperacao().stream().sorted().toList());
        }
    }
}