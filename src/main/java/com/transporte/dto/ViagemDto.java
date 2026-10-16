package com.transporte.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.transporte.model.Viagem;
import com.transporte.model.enums.StatusViagem;

import jakarta.validation.constraints.*;

public final class ViagemDto {
    private ViagemDto() {}

    private static final String HORA = "^([01]\\d|2[0-3]):[0-5]\\d$";

    public record Request(
            @NotNull LocalDate dataPartida,
            @NotNull @Pattern(regexp = HORA, message = "Hora no formato HH:mm") String horaPartida,
            @NotNull @Pattern(regexp = HORA, message = "Hora no formato HH:mm") String horaChegada,
            String observacoes,
            @NotNull Integer idR,
            @NotBlank @Size(min = 5, max = 15) String matricula) {}

    /** PATCH: todos os campos opcionais. */
    public record UpdateRequest(
            @Pattern(regexp = HORA, message = "Hora no formato HH:mm") String horaPartida,
            @Pattern(regexp = HORA, message = "Hora no formato HH:mm") String horaChegada,
            String observacoes) {}

    public record StatusRequest(@NotNull StatusViagem statusViagem) {}

    public record RotaResumo(Integer id, String origem, String destino, BigDecimal precoBase) {}

    public record VeiculoResumo(String matricula, String marca, String modelo, Integer capacidadeMax) {}

    public record Response(Integer id, StatusViagem statusViagem, LocalDate dataPartida, String horaPartida,
                           String horaChegada, String observacoes, RotaResumo rota, VeiculoResumo veiculo) {
        public static Response from(Viagem v) {
            var r = v.getRota();
            var ve = v.getVeiculo();
            return new Response(v.getId(), v.getStatusViagem(), v.getDataPartida(), v.getHoraPartida(),
                    v.getHoraChegada(), v.getObservacoes(),
                    new RotaResumo(r.getId(), r.getOrigem(), r.getDestino(), r.getPrecoBase()),
                    new VeiculoResumo(ve.getMatricula(), ve.getModelo().getMarca(), ve.getModelo().getModelo(),
                            ve.getModelo().getCapacidadeMax()));
        }
    }

    public record Assentos(Integer idV, int capacidade, List<Integer> ocupados, List<Integer> livres, int totalLivres) {}
}