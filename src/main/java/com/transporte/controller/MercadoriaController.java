package com.transporte.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import com.transporte.dto.MercadoriaDto.Request;
import com.transporte.dto.MercadoriaDto.Response;
import com.transporte.exception.ApiException;
import com.transporte.model.Mercadoria;
import com.transporte.repository.MercadoriaRepository;
import com.transporte.repository.PassageiroRepository;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/mercadorias")
@RequiredArgsConstructor
@Transactional
public class MercadoriaController {

    private final MercadoriaRepository repo;
    private final PassageiroRepository passageiros;

    @GetMapping
    public Page<Response> listar(@RequestParam(required = false) Integer idP,
                                 @PageableDefault(size = 20, sort = "id") Pageable pageable) {
        Page<Mercadoria> page = idP == null ? repo.findAll(pageable) : repo.findByPassageiro_Id(idP, pageable);
        return page.map(Response::from);
    }

    @GetMapping("/{id}")
    public Response obter(@PathVariable Integer id) {
        return Response.from(buscar(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Response criar(@Valid @RequestBody Request r) {
        Mercadoria m = new Mercadoria();
        aplicar(m, r);
        return Response.from(repo.save(m));
    }

    @PutMapping("/{id}")
    public Response atualizar(@PathVariable Integer id, @Valid @RequestBody Request r) {
        Mercadoria m = buscar(id);
        aplicar(m, r);
        return Response.from(m);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Integer id) {
        repo.delete(buscar(id));
    }

    private Mercadoria buscar(Integer id) {
        return repo.findById(id).orElseThrow(() -> ApiException.notFound("Mercadoria não encontrada"));
    }

    private void aplicar(Mercadoria m, Request r) {
        m.setPeso(r.peso());
        m.setTipo(r.tipo());
        m.setPassageiro(passageiros.findById(r.idP())
                .orElseThrow(() -> ApiException.notFound("Passageiro não encontrado")));
    }
}