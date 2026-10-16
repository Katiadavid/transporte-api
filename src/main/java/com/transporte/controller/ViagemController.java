package com.transporte.controller;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.transporte.dto.ViagemDto;
import com.transporte.dto.ViagemDto.Response;
import com.transporte.model.enums.StatusViagem;
import com.transporte.service.ViagemService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/viagens")
@RequiredArgsConstructor
public class ViagemController {

    private final ViagemService service;

    /** GET /api/viagens?origem=Maputo&destino=Xai&data=2026-10-20 */
    @GetMapping
    public Page<Response> pesquisar(
            @RequestParam(required = false) String origem,
            @RequestParam(required = false) String destino,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data,
            @RequestParam(required = false) StatusViagem statusViagem,
            @RequestParam(required = false) Integer idR,
            @PageableDefault(size = 20, sort = {"dataPartida", "horaPartida"}) Pageable pageable) {
        return service.pesquisar(origem, destino, data, statusViagem, idR, pageable);
    }

    @GetMapping("/{id}")
    public Response obter(@PathVariable Integer id) {
        return service.obter(id);
    }

    @GetMapping("/{id}/assentos")
    public ViagemDto.Assentos assentos(@PathVariable Integer id) {
        return service.assentos(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Response criar(@Valid @RequestBody ViagemDto.Request r) {
        return service.criar(r);
    }

    @PatchMapping("/{id}")
    public Response atualizar(@PathVariable Integer id, @Valid @RequestBody ViagemDto.UpdateRequest r) {
        return service.atualizar(id, r);
    }

    @PatchMapping("/{id}/status")
    public Response mudarStatus(@PathVariable Integer id, @Valid @RequestBody ViagemDto.StatusRequest r) {
        return service.mudarStatus(id, r.statusViagem());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Integer id) {
        service.remover(id);
    }
}