package com.transporte.dto;

import java.time.LocalDate;
import java.util.Set;
import java.util.TreeSet;

import com.transporte.model.Passageiro;
import com.transporte.model.enums.TipoPassageiro;

import jakarta.validation.constraints.*;

public final class PassageiroDto {
    private PassageiroDto() {}

    public record Request(
            @NotNull @Pattern(regexp = "\\d{9}", message = "NUIT deve ter 9 dígitos") String nuit,
            @NotBlank @Size(min = 2, max = 120) String nome,
            @NotNull @Pattern(regexp = "\\+?\\d{9,15}", message = "Telefone inválido") String telefone,
            @NotNull TipoPassageiro tipoPassageiro,
            @NotNull @Past LocalDate dataNascimento,
            Set<@NotBlank @Size(min = 3, max = 200) String> enderecos) {}

    public record Response(Integer id, String nuit, String nome, String telefone,
                           TipoPassageiro tipoPassageiro, LocalDate dataNascimento, Set<String> enderecos) {
        public static Response from(Passageiro p) {
            return new Response(p.getId(), p.getNuit(), p.getNome(), p.getTelefone(),
                    p.getTipoPassageiro(), p.getDataNascimento(), new TreeSet<>(p.getEnderecos()));
        }
    }
}