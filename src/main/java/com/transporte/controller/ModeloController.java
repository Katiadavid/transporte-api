package com.transporte.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import com.transporte.dto.ModeloDto.Request;
import com.transporte.dto.ModeloDto.Response;
import com.transporte.exception.ApiException;
import com.transporte.model.ModeloVeiculo;
import com.transporte.repository.ModeloVeiculoRepository;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/modelos")
@RequiredArgsConstructor
@Transactional
public class ModeloController {

    private final ModeloVeiculoRepository repo;

    @GetMapping
    public Page<Response> listar(@RequestParam(required = false) String q,
                                 @PageableDefault(size = 20, sort = "id") Pageable pageable) {
        Page<ModeloVeiculo> page = (q == null || q.isBlank())
                ? repo.findAll(pageable)
                : repo.findByMarcaContainingIgnoreCaseOrModeloContainingIgnoreCase(q, q, pageable);
        return page.map(Response::from);
    }

    @GetMapping("/{id}")
    public Response obter(@PathVariable Integer id) {
        return Response.from(buscar(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Response criar(@Valid @RequestBody Request r) {
        if (repo.existsByMarcaAndModelo(r.marca(), r.modelo()))
            throw ApiException.conflict("Modelo já existe para esta marca");
        ModeloVeiculo m = new ModeloVeiculo();
        aplicar(m, r);
        return Response.from(repo.save(m));
    }

    @PutMapping("/{id}")
    public Response atualizar(@PathVariable Integer id, @Valid @RequestBody Request r) {
        ModeloVeiculo m = buscar(id);
        aplicar(m, r);
        return Response.from(m);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Integer id) {
        repo.delete(buscar(id));
        repo.flush();
    }

    private ModeloVeiculo buscar(Integer id) {
        return repo.findById(id).orElseThrow(() -> ApiException.notFound("Modelo não encontrado"));
    }

    private void aplicar(ModeloVeiculo m, Request r) {
        m.setModelo(r.modelo());
        m.setMarca(r.marca());
        m.setTipoVeiculo(r.tipoVeiculo());
        m.setCapacidadeMax(r.capacidadeMax());
    }
}