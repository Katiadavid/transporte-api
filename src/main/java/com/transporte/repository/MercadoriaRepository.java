package com.transporte.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.transporte.model.Mercadoria;

public interface MercadoriaRepository extends JpaRepository<Mercadoria, Integer> {
    Page<Mercadoria> findByPassageiro_Id(Integer idP, Pageable pageable);
}