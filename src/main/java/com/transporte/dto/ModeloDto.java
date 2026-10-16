package com.transporte.dto;

import com.transporte.model.ModeloVeiculo;
import com.transporte.model.enums.TipoVeiculo;

import jakarta.validation.constraints.*;

public final class ModeloDto {
    private ModeloDto() {}

    public record Request(
            @NotBlank @Size(max = 60) String modelo,
            @NotBlank @Size(max = 60) String marca,
            @NotNull TipoVeiculo tipoVeiculo,
            @NotNull @Positive Integer capacidadeMax) {}

    public record Response(Integer id, String modelo, String marca, TipoVeiculo tipoVeiculo, Integer capacidadeMax) {
        public static Response from(ModeloVeiculo m) {
            return new Response(m.getId(), m.getModelo(), m.getMarca(), m.getTipoVeiculo(), m.getCapacidadeMax());
        }
    }
}