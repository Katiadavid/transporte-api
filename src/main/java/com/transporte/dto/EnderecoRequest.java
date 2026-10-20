package com.transporte.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record EnderecoRequest(@NotBlank @Size(min = 3, max = 200) String endereco) {}
