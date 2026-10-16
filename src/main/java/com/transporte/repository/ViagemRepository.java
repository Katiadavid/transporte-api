package com.transporte.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.transporte.model.Viagem;
import com.transporte.model.enums.StatusViagem;

public interface ViagemRepository extends JpaRepository<Viagem, Integer>, JpaSpecificationExecutor<Viagem> {

    /** Viagens do mesmo veículo, no mesmo dia, com horário sobreposto (exclui canceladas). */
    @Query("""
           select v from Viagem v
           where v.veiculo.matricula = :matricula
             and v.dataPartida = :data
             and v.statusViagem <> :cancelada
             and v.horaPartida < :fim
             and v.horaChegada > :inicio
           """)
    List<Viagem> conflitos(@Param("matricula") String matricula,
                           @Param("data") LocalDate data,
                           @Param("inicio") String inicio,
                           @Param("fim") String fim,
                           @Param("cancelada") StatusViagem cancelada);
}