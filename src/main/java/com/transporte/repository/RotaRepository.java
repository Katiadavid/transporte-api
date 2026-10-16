package com.transporte.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.transporte.model.Rota;

public interface RotaRepository extends JpaRepository<Rota, Integer> {
    Page<Rota> findByOrigemContainingIgnoreCaseOrDestinoContainingIgnoreCase(
            String origem, String destino, Pageable pageable);

    boolean existsByIdAndVeiculos_Matricula(Integer id, String matricula);
}