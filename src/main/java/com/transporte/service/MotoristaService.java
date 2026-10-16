package com.transporte.service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.transporte.dto.MotoristaDto.Request;
import com.transporte.dto.MotoristaDto.Response;
import com.transporte.exception.ApiException;
import com.transporte.model.Motorista;
import com.transporte.model.enums.StatusMotorista;
import com.transporte.repository.MotoristaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class MotoristaService {

    private final MotoristaRepository repo;

    @Transactional(readOnly = true)
    public Page<Response> listar(String q, Pageable pageable) {
        Page<Motorista> page = (q == null || q.isBlank())
                ? repo.findAll(pageable)
                : repo.findByNomeContainingIgnoreCaseOrCartaDeConducaoContainingIgnoreCaseOrTelefoneContaining(q, q, q, pageable);
        return page.map(Response::from);
    }

    @Transactional(readOnly = true)
    public Response obter(Integer id) {
        return Response.from(buscar(id));
    }

    /** Motoristas ativos com carta a expirar nos próximos N dias. */
    @Transactional(readOnly = true)
    public List<Response> cartaAExpirar(int dias) {
        LocalDate limite = LocalDate.now().plusDays(dias);
        return repo.findByDataValidadeCartaLessThanEqualAndStatusMotoristaOrderByDataValidadeCartaAsc(
                limite, StatusMotorista.ATIVO).stream().map(Response::from).toList();
    }

    public Response criar(Request r) {
        if (repo.existsByCartaDeConducao(r.cartaDeConducao()))
            throw ApiException.conflict("Já existe um motorista com esta carta de condução");
        Motorista m = new Motorista();
        aplicar(m, r);
        return Response.from(repo.save(m));
    }

    public Response atualizar(Integer id, Request r) {
        Motorista m = buscar(id);
        if (!m.getCartaDeConducao().equals(r.cartaDeConducao()) && repo.existsByCartaDeConducao(r.cartaDeConducao()))
            throw ApiException.conflict("Já existe um motorista com esta carta de condução");
        aplicar(m, r);
        return Response.from(m);
    }

    public void remover(Integer id) {
        repo.delete(buscar(id));
        repo.flush();
    }

    public Response adicionarEndereco(Integer id, String endereco) {
        Motorista m = buscar(id);
        if (!m.getEnderecos().add(endereco)) throw ApiException.conflict("Endereço já registado");
        return Response.from(m);
    }

    public void removerEndereco(Integer id, String endereco) {
        Motorista m = buscar(id);
        if (!m.getEnderecos().remove(endereco)) throw ApiException.notFound("Endereço não encontrado");
    }

    private Motorista buscar(Integer id) {
        return repo.findById(id).orElseThrow(() -> ApiException.notFound("Motorista não encontrado"));
    }

    private void aplicar(Motorista m, Request r) {
        m.setDataContratacao(r.dataContratacao());
        m.setNome(r.nome());
        m.setCartaDeConducao(r.cartaDeConducao());
        m.setDataValidadeCarta(r.dataValidadeCarta());
        m.setTelefone(r.telefone());
        m.setStatusMotorista(r.statusMotorista() != null ? r.statusMotorista() : StatusMotorista.ATIVO);
        m.getEnderecos().clear();
        if (r.enderecos() != null) m.getEnderecos().addAll(new HashSet<>(r.enderecos()));
    }
}