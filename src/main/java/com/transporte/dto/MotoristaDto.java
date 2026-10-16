package com.transporte.dto;

import java.time.LocalDate;
import java.util.Set;
import java.util.TreeSet;

import com.transporte.model.Motorista;
import com.transporte.model.enums.StatusMotorista;

import jakarta.validation.constraints.*;

public final class MotoristaDto {
    private MotoristaDto() {}

    public record Request(
            @NotNull LocalDate dataContratacao,
            @NotBlank @Size(min = 2, max = 120) String nome,
            @NotBlank @Size(min = 4, max = 30) String cartaDeConducao,
            @NotNull LocalDate dataValidadeCarta,
            @NotNull @Pattern(regexp = "\\+?\\d{9,15}", message = "Telefone inválido") String telefone,
            StatusMotorista statusMotorista,
            Set<@NotBlank @Size(min = 3, max = 200) String> enderecos) {}

    public record Response(Integer id, LocalDate dataContratacao, String nome, String cartaDeConducao,
                           LocalDate dataValidadeCarta, String telefone, StatusMotorista statusMotorista,
                           Set<String> enderecos) {
        public static Response from(Motorista m) {
            return new Response(m.getId(), m.getDataContratacao(), m.getNome(), m.getCartaDeConducao(),
                    m.getDataValidadeCarta(), m.getTelefone(), m.getStatusMotorista(),
                    new TreeSet<>(m.getEnderecos()));
        }
    }
}