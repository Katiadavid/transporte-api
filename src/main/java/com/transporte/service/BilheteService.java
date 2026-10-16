package com.transporte.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.transporte.dto.BilheteDto.Request;
import com.transporte.dto.BilheteDto.Response;
import com.transporte.exception.ApiException;
import com.transporte.model.Bilhete;
import com.transporte.model.Passageiro;
import com.transporte.model.Viagem;
import com.transporte.model.enums.StatusBilhete;
import com.transporte.model.enums.StatusViagem;
import com.transporte.repository.BilheteRepository;
import com.transporte.repository.PassageiroRepository;
import com.transporte.repository.ViagemRepository;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class BilheteService {

    private static final DateTimeFormatter HORA = DateTimeFormatter.ofPattern("HH:mm:ss");

    private final BilheteRepository repo;
    private final ViagemRepository viagens;
    private final PassageiroRepository passageiros;

    @Transactional(readOnly = true)
    public Page<Response> listar(Integer idP, Integer idV, StatusBilhete status, Pageable pageable) {
        Specification<Bilhete> spec = (root, query, cb) -> {
            List<Predicate> p = new ArrayList<>();
            if (idP != null) p.add(cb.equal(root.get("passageiro").get("id"), idP));
            if (idV != null) p.add(cb.equal(root.get("viagem").get("id"), idV));
            if (status != null) p.add(cb.equal(root.get("statusBilhete"), status));
            return cb.and(p.toArray(new Predicate[0]));
        };
        return repo.findAll(spec, pageable).map(Response::from);
    }

    @Transactional(readOnly = true)
    public Response obter(Integer id) {
        return Response.from(buscar(id));
    }

    /** Compra com isolamento SERIALIZABLE para impedir o mesmo assento vendido duas vezes. */
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public Response comprar(Request r) {
        Viagem viagem = viagens.findById(r.idV())
                .orElseThrow(() -> ApiException.notFound("Viagem não encontrada"));
        if (viagem.getStatusViagem() != StatusViagem.AGENDADA)
            throw ApiException.conflict("Viagem não está disponível para venda");

        Passageiro passageiro = passageiros.findById(r.idP())
                .orElseThrow(() -> ApiException.notFound("Passageiro não encontrado"));

        int capacidade = viagem.getVeiculo().getModelo().getCapacidadeMax();
        if (r.numeroAssento() > capacidade)
            throw ApiException.unprocessable("Assento inválido: o veículo tem " + capacidade + " lugares");

        if (repo.existsByViagem_IdAndNumeroAssentoAndStatusBilheteIn(
                r.idV(), r.numeroAssento(), EnumSet.of(StatusBilhete.ATIVO, StatusBilhete.USADO)))
            throw ApiException.conflict("Assento " + r.numeroAssento() + " já está ocupado");

        if (repo.existsByViagem_IdAndPassageiro_IdAndStatusBilhete(r.idV(), r.idP(), StatusBilhete.ATIVO))
            throw ApiException.conflict("Passageiro já tem bilhete ativo nesta viagem");

        BigDecimal preco = viagem.getRota().getPrecoBase()
                .multiply(passageiro.getTipoPassageiro().getFator())
                .setScale(2, RoundingMode.HALF_UP);

        Bilhete b = new Bilhete();
        b.setPassageiro(passageiro);
        b.setViagem(viagem);
        b.setNumeroAssento(r.numeroAssento());
        b.setMetodoPagamento(r.metodoPagamento());
        b.setPrecoPago(preco);
        b.setStatusBilhete(StatusBilhete.ATIVO);
        b.setDataCompra(LocalDate.now());
        b.setHoraCompra(LocalTime.now().format(HORA));
        b.setDataValidade(viagem.getDataPartida());
        return Response.from(repo.save(b));
    }

    public Response cancelar(Integer id) {
        Bilhete b = buscar(id);
        if (b.getStatusBilhete() != StatusBilhete.ATIVO)
            throw ApiException.conflict("Bilhete " + b.getStatusBilhete().name().toLowerCase() + " não pode ser cancelado");
        if (b.getViagem().getStatusViagem() != StatusViagem.AGENDADA)
            throw ApiException.conflict("Só é possível cancelar bilhetes de viagens agendadas");
        b.setStatusBilhete(StatusBilhete.CANCELADO);
        return Response.from(b);
    }

    /** Marca como EXPIRADO os bilhetes ativos cuja validade já passou (pode ser chamado por um agendador). */
    public Map<String, Integer> expirar() {
        int n = repo.expirar(LocalDate.now(), StatusBilhete.ATIVO, StatusBilhete.EXPIRADO);
        return Map.of("expirados", n);
    }

    private Bilhete buscar(Integer id) {
        return repo.findById(id).orElseThrow(() -> ApiException.notFound("Bilhete não encontrado"));
    }
}