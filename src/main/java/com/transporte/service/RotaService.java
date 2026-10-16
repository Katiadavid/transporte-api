package com.transporte.service;

import java.util.HashSet;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.transporte.dto.RotaDto.Request;
import com.transporte.dto.RotaDto.Response;
import com.transporte.exception.ApiException;
import com.transporte.model.Rota;
import com.transporte.model.enums.DiaSemana;
import com.transporte.repository.RotaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class RotaService {

    private final RotaRepository repo;

    @Transactional(readOnly = true)
    public Page<Response> listar(String q, Pageable pageable) {
        Page<Rota> page = (q == null || q.isBlank())
                ? repo.findAll(pageable)
                : repo.findByOrigemContainingIgnoreCaseOrDestinoContainingIgnoreCase(q, q, pageable);
        return page.map(Response::from);
    }

    @Transactional(readOnly = true)
    public Response obter(Integer id) {
        return Response.from(buscar(id));
    }

    public Response criar(Request r) {
        Rota rota = new Rota();
        aplicar(rota, r);
        return Response.from(repo.save(rota));
    }

    public Response atualizar(Integer id, Request r) {
        Rota rota = buscar(id);
        aplicar(rota, r);
        return Response.from(rota);
    }

    public void remover(Integer id) {
        repo.delete(buscar(id));
        repo.flush();
    }

    public Response adicionarDia(Integer id, DiaSemana dia) {
        Rota rota = buscar(id);
        if (!rota.getDiasOperacao().add(dia)) throw ApiException.conflict("A rota já opera neste dia");
        return Response.from(rota);
    }

    public void removerDia(Integer id, DiaSemana dia) {
        Rota rota = buscar(id);
        if (!rota.getDiasOperacao().remove(dia)) throw ApiException.notFound("A rota não opera neste dia");
        if (rota.getDiasOperacao().isEmpty()) throw ApiException.unprocessable("A rota deve operar pelo menos um dia");
    }

    private Rota buscar(Integer id) {
        return repo.findById(id).orElseThrow(() -> ApiException.notFound("Rota não encontrada"));
    }

    private void aplicar(Rota rota, Request r) {
        if (r.origem().equalsIgnoreCase(r.destino()))
            throw ApiException.unprocessable("Origem e destino devem ser diferentes");
        rota.setOrigem(r.origem());
        rota.setDestino(r.destino());
        rota.setDuracaoEstimada(r.duracaoEstimada());
        rota.setPrecoBase(r.precoBase());
        rota.setDistanciaKm(r.distanciaKm());
        rota.getDiasOperacao().clear();
        rota.getDiasOperacao().addAll(new HashSet<>(r.diasOperacao()));
    }
}