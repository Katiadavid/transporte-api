package com.transporte.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.transporte.model.Motorista;
import com.transporte.model.enums.StatusMotorista;

public interface MotoristaRepository extends JpaRepository<Motorista, Integer> {
    boolean existsByCartaDeConducao(String cartaDeConducao);

    Page<Motorista> findByNomeContainingIgnoreCaseOrCartaDeConducaoContainingIgnoreCaseOrTelefoneContaining(
            String nome, String carta, String telefone, Pageable pageable);

    List<Motorista> findByDataValidadeCartaLessThanEqualAndStatusMotoristaOrderByDataValidadeCartaAsc(
            LocalDate limite, StatusMotorista status);
}