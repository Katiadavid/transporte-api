package com.transporte.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.transporte.model.ModeloVeiculo;

public interface ModeloVeiculoRepository extends JpaRepository<ModeloVeiculo, Integer> {
    boolean existsByMarcaAndModelo(String marca, String modelo);

    Page<ModeloVeiculo> findByMarcaContainingIgnoreCaseOrModeloContainingIgnoreCase(
            String marca, String modelo, Pageable pageable);
}