package com.transporte.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.IntStream;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.transporte.dto.ViagemDto;
import com.transporte.dto.ViagemDto.Response;
import com.transporte.exception.ApiException;
import com.transporte.model.Rota;
import com.transporte.model.Veiculo;
import com.transporte.model.Viagem;
import com.transporte.model.enums.DiaSemana;
import com.transporte.model.enums.StatusBilhete;
import com.transporte.model.enums.StatusVeiculo;
import com.transporte.model.enums.StatusViagem;
import com.transporte.repository.BilheteRepository;
import com.transporte.repository.RotaRepository;
import com.transporte.repository.VeiculoRepository;
import com.transporte.repository.ViagemRepository;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ViagemService {

    private final ViagemRepository repo;
    private final RotaRepository rotas;
    private final VeiculoRepository veiculos;
    private final BilheteRepository bilhetes;

    @Transactional(readOnly = true)
    public Page<Response> pesquisar(String origem, String destino, LocalDate data,
                                    StatusViagem status, Integer idR, Pageable pageable) {
        Specification<Viagem> spec = (root, query, cb) -> {
            List<Predicate> p = new ArrayList<>();
            if (origem != null && !origem.isBlank())
                p.add(cb.like(cb.lower(root.get("rota").get("origem")), "%" + origem.toLowerCase() + "%"));
            if (destino != null && !destino.isBlank())
                p.add(cb.like(cb.lower(root.get("rota").get("destino")), "%" + destino.toLowerCase() + "%"));
            if (data != null) p.add(cb.equal(root.get("dataPartida"), data));
            if (status != null) p.add(cb.equal(root.get("statusViagem"), status));
            if (idR != null) p.add(cb.equal(root.get("rota").get("id"), idR));
            return cb.and(p.toArray(new Predicate[0]));
        };
        return repo.findAll(spec, pageable).map(Response::from);
    }

    @Transactional(readOnly = true)
    public Response obter(Integer id) {
        return Response.from(buscar(id));
    }

    @Transactional(readOnly = true)
    public ViagemDto.Assentos assentos(Integer id) {
        Viagem v = buscar(id);
        int capacidade = v.getVeiculo().getModelo().getCapacidadeMax();
        List<Integer> ocupados = bilhetes.assentosOcupados(id, EnumSet.of(StatusBilhete.ATIVO, StatusBilhete.USADO));
        List<Integer> livres = IntStream.rangeClosed(1, capacidade).boxed()
                .filter(n -> !ocupados.contains(n)).toList();
        return new ViagemDto.Assentos(id, capacidade, ocupados, livres, livres.size());
    }

    public Response criar(ViagemDto.Request r) {
        if (r.horaChegada().compareTo(r.horaPartida()) <= 0)
            throw ApiException.unprocessable("horaChegada deve ser posterior a horaPartida");

        Rota rota = rotas.findById(r.idR()).orElseThrow(() -> ApiException.notFound("Rota não encontrada"));
        DiaSemana dia = DiaSemana.de(r.dataPartida().getDayOfWeek());
        if (!rota.getDiasOperacao().contains(dia))
            throw ApiException.unprocessable("A rota não opera à " + dia);

        String matricula = r.matricula().trim().toUpperCase();
        Veiculo veiculo = veiculos.findById(matricula)
                .orElseThrow(() -> ApiException.notFound("Veículo não encontrado"));
        if (veiculo.getStatusVeiculo() != StatusVeiculo.ATIVO) throw ApiException.conflict("Veículo não está ativo");
        if (!rotas.existsByIdAndVeiculos_Matricula(r.idR(), matricula))
            throw ApiException.unprocessable("Veículo não está associado a esta rota");

        List<Viagem> conflitos = repo.conflitos(matricula, r.dataPartida(), r.horaPartida(), r.horaChegada(),
                StatusViagem.CANCELADA);
        if (!conflitos.isEmpty())
            throw ApiException.conflict("Veículo já tem a viagem #" + conflitos.get(0).getId() + " nesse horário");

        Viagem v = new Viagem();
        v.setRota(rota);
        v.setVeiculo(veiculo);
        v.setDataPartida(r.dataPartida());
        v.setHoraPartida(r.horaPartida());
        v.setHoraChegada(r.horaChegada());
        v.setObservacoes(r.observacoes());
        return Response.from(repo.save(v));
    }

    public Response atualizar(Integer id, ViagemDto.UpdateRequest r) {
        Viagem v = buscar(id);
        if (r.horaPartida() != null) v.setHoraPartida(r.horaPartida());
        if (r.horaChegada() != null) v.setHoraChegada(r.horaChegada());
        if (r.observacoes() != null) v.setObservacoes(r.observacoes());
        if (v.getHoraChegada().compareTo(v.getHoraPartida()) <= 0)
            throw ApiException.unprocessable("horaChegada deve ser posterior a horaPartida");
        return Response.from(v);
    }

    public Response mudarStatus(Integer id, StatusViagem novo) {
        Viagem v = buscar(id);
        if (!v.getStatusViagem().podeIrPara(novo))
            throw ApiException.conflict("Transição inválida: " + v.getStatusViagem() + " → " + novo);

        if (novo == StatusViagem.CANCELADA)
            bilhetes.atualizarStatusPorViagem(id, StatusBilhete.ATIVO, StatusBilhete.CANCELADO);
        if (novo == StatusViagem.CONCLUIDA)
            bilhetes.atualizarStatusPorViagem(id, StatusBilhete.ATIVO, StatusBilhete.USADO);

        v.setStatusViagem(novo);
        return Response.from(v);
    }

    public void remover(Integer id) {
        repo.delete(buscar(id));
        repo.flush();
    }

    private Viagem buscar(Integer id) {
        return repo.findById(id).orElseThrow(() -> ApiException.notFound("Viagem não encontrada"));
    }
}