package com.transporte.service;

import java.util.HashSet;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.transporte.dto.PassageiroDto.Request;
import com.transporte.dto.PassageiroDto.Response;
import com.transporte.exception.ApiException;
import com.transporte.model.Passageiro;
import com.transporte.repository.PassageiroRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class PassageiroService {

    private final PassageiroRepository repo;

    @Transactional(readOnly = true)
    public Page<Response> listar(String q, Pageable pageable) {
        Page<Passageiro> page = (q == null || q.isBlank())
                ? repo.findAll(pageable)
                : repo.findByNomeContainingIgnoreCaseOrNuitContainingOrTelefoneContaining(q, q, q, pageable);
        return page.map(Response::from);
    }

    @Transactional(readOnly = true)
    public Response obter(Integer id) {
        return Response.from(buscar(id));
    }

    public Response criar(Request r) {
        if (repo.existsByNuit(r.nuit())) throw ApiException.conflict("Já existe um passageiro com este NUIT");
        Passageiro p = new Passageiro();
        aplicar(p, r);
        return Response.from(repo.save(p));
    }

    public Response atualizar(Integer id, Request r) {
        Passageiro p = buscar(id);
        if (!p.getNuit().equals(r.nuit()) && repo.existsByNuit(r.nuit()))
            throw ApiException.conflict("Já existe um passageiro com este NUIT");
        aplicar(p, r);
        return Response.from(p);
    }

    public void remover(Integer id) {
        repo.delete(buscar(id));
        repo.flush();
    }

    public Response adicionarEndereco(Integer id, String endereco) {
        Passageiro p = buscar(id);
        if (!p.getEnderecos().add(endereco)) throw ApiException.conflict("Endereço já registado");
        return Response.from(p);
    }

    public void removerEndereco(Integer id, String endereco) {
        Passageiro p = buscar(id);
        if (!p.getEnderecos().remove(endereco)) throw ApiException.notFound("Endereço não encontrado");
    }

    private Passageiro buscar(Integer id) {
        return repo.findById(id).orElseThrow(() -> ApiException.notFound("Passageiro não encontrado"));
    }

    private void aplicar(Passageiro p, Request r) {
        p.setNuit(r.nuit());
        p.setNome(r.nome());
        p.setTelefone(r.telefone());
        p.setTipoPassageiro(r.tipoPassageiro());
        p.setDataNascimento(r.dataNascimento());
        p.getEnderecos().clear();
        if (r.enderecos() != null) p.getEnderecos().addAll(new HashSet<>(r.enderecos()));
    }
}