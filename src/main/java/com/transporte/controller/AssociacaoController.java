 package com.transporte.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.transporte.dto.AssociacaoDto.MotoristaRef;
import com.transporte.dto.AssociacaoDto.VeiculoRef;
import com.transporte.dto.MotoristaDto;
import com.transporte.dto.VeiculoDto;
import com.transporte.service.AssociacaoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AssociacaoController {

    private final AssociacaoService service;

    /* Rota <-> Motorista */
    @GetMapping("/rotas/{idR}/motoristas")
    public List<MotoristaDto.Response> motoristasDaRota(@PathVariable Integer idR) {
        return service.motoristasDaRota(idR);
    }

    @PostMapping("/rotas/{idR}/motoristas")
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, Object> associarMotoristaARota(@PathVariable Integer idR, @Valid @RequestBody MotoristaRef r) {
        service.associarMotoristaARota(idR, r.idM());
        return Map.of("idR", idR, "idM", r.idM());
    }

    @DeleteMapping("/rotas/{idR}/motoristas/{idM}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removerMotoristaDaRota(@PathVariable Integer idR, @PathVariable Integer idM) {
        service.removerMotoristaDaRota(idR, idM);
    }

    /* Rota <-> Veículo */
    @GetMapping("/rotas/{idR}/veiculos")
    public List<VeiculoDto.Response> veiculosDaRota(@PathVariable Integer idR) {
        return service.veiculosDaRota(idR);
    }

    @PostMapping("/rotas/{idR}/veiculos")
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, Object> associarVeiculoARota(@PathVariable Integer idR, @Valid @RequestBody VeiculoRef r) {
        service.associarVeiculoARota(idR, r.matricula());
        return Map.of("idR", idR, "matricula", r.matricula().trim().toUpperCase());
    }

    @DeleteMapping("/rotas/{idR}/veiculos/{matricula}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removerVeiculoDaRota(@PathVariable Integer idR, @PathVariable String matricula) {
        service.removerVeiculoDaRota(idR, matricula);
    }

    /* Motorista <-> Veículo */
    @GetMapping("/motoristas/{idM}/veiculos")
    public List<VeiculoDto.Response> veiculosDoMotorista(@PathVariable Integer idM) {
        return service.veiculosDoMotorista(idM);
    }

    @PostMapping("/motoristas/{idM}/veiculos")
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, Object> associarVeiculoAMotorista(@PathVariable Integer idM, @Valid @RequestBody VeiculoRef r) {
        service.associarVeiculoAMotorista(idM, r.matricula());
        return Map.of("idM", idM, "matricula", r.matricula().trim().toUpperCase());
    }

    @DeleteMapping("/motoristas/{idM}/veiculos/{matricula}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removerVeiculoDoMotorista(@PathVariable Integer idM, @PathVariable String matricula) {
        service.removerVeiculoDoMotorista(idM, matricula);
    }
}