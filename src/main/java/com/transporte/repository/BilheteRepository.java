package com.transporte.repository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.transporte.model.Bilhete;
import com.transporte.model.enums.StatusBilhete;

public interface BilheteRepository extends JpaRepository<Bilhete, Integer>, JpaSpecificationExecutor<Bilhete> {

    boolean existsByViagem_IdAndNumeroAssentoAndStatusBilheteIn(
            Integer idV, Integer numeroAssento, Collection<StatusBilhete> status);

    boolean existsByViagem_IdAndPassageiro_IdAndStatusBilhete(
            Integer idV, Integer idP, StatusBilhete status);

    @Query("""
           select b.numeroAssento from Bilhete b
           where b.viagem.id = :idV and b.statusBilhete in :status
           order by b.numeroAssento
           """)
    List<Integer> assentosOcupados(@Param("idV") Integer idV,
                                   @Param("status") Collection<StatusBilhete> status);

    @Modifying(flushAutomatically = true)
    @Query("update Bilhete b set b.statusBilhete = :novo where b.viagem.id = :idV and b.statusBilhete = :atual")
    int atualizarStatusPorViagem(@Param("idV") Integer idV,
                                 @Param("atual") StatusBilhete atual,
                                 @Param("novo") StatusBilhete novo);

    @Modifying(flushAutomatically = true)
    @Query("update Bilhete b set b.statusBilhete = :novo where b.statusBilhete = :atual and b.dataValidade < :hoje")
    int expirar(@Param("hoje") LocalDate hoje,
                @Param("atual") StatusBilhete atual,
                @Param("novo") StatusBilhete novo);
}