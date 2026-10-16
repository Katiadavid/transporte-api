package com.transporte.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public final class AssociacaoDto {
    private AssociacaoDto() {}

    public record MotoristaRef(@NotNull Integer idM) {}

    public record VeiculoRef(@NotBlank @Size(min = 5, max = 15) String matricula) {}
}