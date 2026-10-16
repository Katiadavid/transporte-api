package com.transporte.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.transporte.dto.RotaDto.DiaRequest;
import com.transporte.dto.RotaDto.Request;
import com.transporte.dto.RotaDto.Response;
import com.transporte.model.enums.DiaSemana;
import com.transporte.service.RotaService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/rotas")
@RequiredArgsConstructor
public class RotaController {

    private final RotaService service;

    @GetMapping
    public Page<Response> listar(@RequestParam(required = false) String q,
                                 @PageableDefault(size = 20, sort = "id") Pageable pageable) {
        return service.listar(q, pageable);
    }

    @GetMapping("/{id}")
    public Response obter(@PathVariable Integer id) {
        return service.obter(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Response criar(@Valid @RequestBody Request r) {
        return service.criar(r);
    }

    @PutMapping("/{id}")
    public Response atualizar(@PathVariable Integer id, @Valid @RequestBody Request r) {
        return service.atualizar(id, r);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Integer id) {
        service.remover(id);
    }

    @PostMapping("/{id}/dias")
    @ResponseStatus(HttpStatus.CREATED)
    public Response adicionarDia(@PathVariable Integer id, @Valid @RequestBody DiaRequest r) {
        return service.adicionarDia(id, r.dia());
    }

    @DeleteMapping("/{id}/dias/{dia}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removerDia(@PathVariable Integer id, @PathVariable DiaSemana dia) {
        service.removerDia(id, dia);
    }
}