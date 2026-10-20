package com.transporte.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.transporte.dto.EnderecoRequest;
import com.transporte.dto.MotoristaDto.Request;
import com.transporte.dto.MotoristaDto.Response;
import com.transporte.service.MotoristaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/motoristas")
public class MotoristaController {

    private final MotoristaService service;

    public MotoristaController(MotoristaService service) {
        this.service = service;
    }

    @GetMapping
    public Page<Response> listar(@RequestParam(required = false) String q,
                                 @PageableDefault(size = 20, sort = "id") Pageable pageable) {
        return service.listar(q, pageable);
    }

    @GetMapping("/alertas/carta-a-expirar")
    public List<Response> cartaAExpirar(@RequestParam(defaultValue = "30") int dias) {
        return service.cartaAExpirar(dias);
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

    @PostMapping("/{id}/enderecos")
    @ResponseStatus(HttpStatus.CREATED)
    public Response adicionarEndereco(@PathVariable Integer id, @Valid @RequestBody EnderecoRequest r) {
        return service.adicionarEndereco(id, r.endereco());
    }

    @DeleteMapping("/{id}/enderecos/{endereco}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removerEndereco(@PathVariable Integer id, @PathVariable String endereco) {
        service.removerEndereco(id, endereco);
    }
}