package com.transporte.dto;

import com.transporte.model.Veiculo;
import com.transporte.model.enums.StatusVeiculo;

import jakarta.validation.constraints.*;

public final class VeiculoDto {
    private VeiculoDto() {}

    public record Request(
            @NotBlank @Size(min = 5, max = 15) String matricula,
            @NotNull Integer idModelo,
            @NotBlank @Size(max = 30) String cor,
            @NotNull @Min(1980) Integer anoFabrico,
            StatusVeiculo statusVeiculo,
            @Min(0) Integer quilometragem) {}

    public record Response(String matricula, ModeloDto.Response modelo, String cor, Integer anoFabrico,
                           StatusVeiculo statusVeiculo, Integer quilometragem) {
        public static Response from(Veiculo v) {
            return new Response(v.getMatricula(), ModeloDto.Response.from(v.getModelo()), v.getCor(),
                    v.getAnoFabrico(), v.getStatusVeiculo(), v.getQuilometragem());
        }
    }
}