package com.transporte.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.transporte.dto.MotoristaDto;
import com.transporte.dto.VeiculoDto;
import com.transporte.exception.ApiException;
import com.transporte.model.Motorista;
import com.transporte.model.Rota;
import com.transporte.model.Veiculo;
import com.transporte.model.enums.StatusMotorista;
import com.transporte.model.enums.StatusVeiculo;
import com.transporte.repository.MotoristaRepository;
import com.transporte.repository.RotaRepository;
import com.transporte.repository.VeiculoRepository;

import lombok.RequiredArgsConstructor;

/** Tabelas N:M: RotaMotorista, RotaVeiculo e MotoristaVeiculo. */
@Service
@RequiredArgsConstructor
@Transactional
public class AssociacaoService {

    private final RotaRepository rotas;
    private final MotoristaRepository motoristas;
    private final VeiculoRepository veiculos;

    /* ---------- Rota <-> Motorista ---------- */

    @Transactional(readOnly = true)
    public List<MotoristaDto.Response> motoristasDaRota(Integer idR) {
        return rota(idR).getMotoristas().stream().map(MotoristaDto.Response::from).toList();
    }

    public void associarMotoristaARota(Integer idR, Integer idM) {
        Rota r = rota(idR);
        Motorista m = motoristaElegivel(idM);
        if (!r.getMotoristas().add(m)) throw ApiException.conflict("Associação já existe");
    }

    public void removerMotoristaDaRota(Integer idR, Integer idM) {
        if (!rota(idR).getMotoristas().removeIf(m -> m.getId().equals(idM)))
            throw ApiException.notFound("Associação não encontrada");
    }

    /* ---------- Rota <-> Veículo ---------- */

    @Transactional(readOnly = true)
    public List<VeiculoDto.Response> veiculosDaRota(Integer idR) {
        return rota(idR).getVeiculos().stream().map(VeiculoDto.Response::from).toList();
    }

    public void associarVeiculoARota(Integer idR, String matricula) {
        Rota r = rota(idR);
        Veiculo v = veiculoElegivel(matricula);
        if (!r.getVeiculos().add(v)) throw ApiException.conflict("Associação já existe");
    }

    public void removerVeiculoDaRota(Integer idR, String matricula) {
        String mat = matricula.toUpperCase();
        if (!rota(idR).getVeiculos().removeIf(v -> v.getMatricula().equals(mat)))
            throw ApiException.notFound("Associação não encontrada");
    }

    /* ---------- Motorista <-> Veículo ---------- */

    @Transactional(readOnly = true)
    public List<VeiculoDto.Response> veiculosDoMotorista(Integer idM) {
        return motorista(idM).getVeiculos().stream().map(VeiculoDto.Response::from).toList();
    }

    public void associarVeiculoAMotorista(Integer idM, String matricula) {
        Motorista m = motoristaElegivel(idM);
        Veiculo v = veiculoElegivel(matricula);
        if (!m.getVeiculos().add(v)) throw ApiException.conflict("Associação já existe");
    }

    public void removerVeiculoDoMotorista(Integer idM, String matricula) {
        String mat = matricula.toUpperCase();
        if (!motorista(idM).getVeiculos().removeIf(v -> v.getMatricula().equals(mat)))
            throw ApiException.notFound("Associação não encontrada");
    }

    /* ---------- helpers ---------- */

    private Rota rota(Integer id) {
        return rotas.findById(id).orElseThrow(() -> ApiException.notFound("Rota não encontrada"));
    }

    private Motorista motorista(Integer id) {
        return motoristas.findById(id).orElseThrow(() -> ApiException.notFound("Motorista não encontrado"));
    }

    private Motorista motoristaElegivel(Integer id) {
        Motorista m = motorista(id);
        if (m.getStatusMotorista() != StatusMotorista.ATIVO) throw ApiException.conflict("Motorista não está ativo");
        if (m.getDataValidadeCarta().isBefore(LocalDate.now())) throw ApiException.conflict("Carta de condução expirada");
        return m;
    }

    private Veiculo veiculoElegivel(String matricula) {
        Veiculo v = veiculos.findById(matricula.trim().toUpperCase())
                .orElseThrow(() -> ApiException.notFound("Veículo não encontrado"));
        if (v.getStatusVeiculo() != StatusVeiculo.ATIVO) throw ApiException.conflict("Veículo não está ativo");
        return v;

    }
}