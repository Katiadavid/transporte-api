package com.transporte.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.transporte.model.Veiculo;

public interface VeiculoRepository extends JpaRepository<Veiculo, String> {
    Page<Veiculo> findByMatriculaContainingIgnoreCaseOrCorContainingIgnoreCase(
            String matricula, String cor, Pageable pageable);
}