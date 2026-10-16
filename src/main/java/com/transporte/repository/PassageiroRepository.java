package com.transporte.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.transporte.model.Passageiro;

public interface PassageiroRepository extends JpaRepository<Passageiro, Integer> {
    boolean existsByNuit(String nuit);

    Page<Passageiro> findByNomeContainingIgnoreCaseOrNuitContainingOrTelefoneContaining(
            String nome, String nuit, String telefone, Pageable pageable);
}