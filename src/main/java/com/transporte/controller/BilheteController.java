package com.transporte.controller;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.transporte.dto.BilheteDto.Request;
import com.transporte.dto.BilheteDto.Response;
import com.transporte.model.enums.StatusBilhete;
import com.transporte.service.BilheteService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/bilhetes")
@RequiredArgsConstructor
public class BilheteController {

    private final BilheteService service;

    @GetMapping
    public Page<Response> listar(@RequestParam(required = false) Integer idP,
                                 @RequestParam(required = false) Integer idV,
                                 @RequestParam(required = false) StatusBilhete statusBilhete,
                                 @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return service.listar(idP, idV, statusBilhete, pageable);
    }

    @GetMapping("/{id}")
    public Response obter(@PathVariable Integer id) {
        return service.obter(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Response comprar(@Valid @RequestBody Request r) {
        return service.comprar(r);
    }

    @PatchMapping("/{id}/cancelar")
    public Response cancelar(@PathVariable Integer id) {
        return service.cancelar(id);
    }

    @PostMapping("/expirar")
    public Map<String, Integer> expirar() {
        return service.expirar();
    }
}