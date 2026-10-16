package com.transporte.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import com.transporte.dto.VeiculoDto.Request;
import com.transporte.dto.VeiculoDto.Response;
import com.transporte.exception.ApiException;
import com.transporte.model.ModeloVeiculo;
import com.transporte.model.Veiculo;
import com.transporte.model.enums.StatusVeiculo;
import com.transporte.repository.ModeloVeiculoRepository;
import com.transporte.repository.VeiculoRepository;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/veiculos")
@RequiredArgsConstructor
@Transactional
public class VeiculoController {

    private final VeiculoRepository repo;
    private final ModeloVeiculoRepository modelos;

    @GetMapping
    public Page<Response> listar(@RequestParam(required = false) String q,
                                 @PageableDefault(size = 20, sort = "matricula") Pageable pageable) {
        Page<Veiculo> page = (q == null || q.isBlank())
                ? repo.findAll(pageable)
                : repo.findByMatriculaContainingIgnoreCaseOrCorContainingIgnoreCase(q, q, pageable);
        return page.map(Response::from);
    }

    @GetMapping("/{matricula}")
    public Response obter(@PathVariable String matricula) {
        return Response.from(buscar(matricula));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Response criar(@Valid @RequestBody Request r) {
        String matricula = r.matricula().trim().toUpperCase();
        if (repo.existsById(matricula)) throw ApiException.conflict("Já existe um veículo com esta matrícula");
        Veiculo v = new Veiculo();
        v.setMatricula(matricula);
        aplicar(v, r);
        return Response.from(repo.save(v));
    }

    @PutMapping("/{matricula}")
    public Response atualizar(@PathVariable String matricula, @Valid @RequestBody Request r) {
        Veiculo v = buscar(matricula);
        aplicar(v, r); // a matrícula (PK) não é alterada
        return Response.from(v);
    }

    @DeleteMapping("/{matricula}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable String matricula) {
        repo.delete(buscar(matricula));
        repo.flush();
    }

    private Veiculo buscar(String matricula) {
        return repo.findById(matricula.trim().toUpperCase())
                .orElseThrow(() -> ApiException.notFound("Veículo não encontrado"));
    }

    private void aplicar(Veiculo v, Request r) {
        ModeloVeiculo modelo = modelos.findById(r.idModelo())
                .orElseThrow(() -> ApiException.notFound("Modelo não encontrado"));
        v.setModelo(modelo);
        v.setCor(r.cor());
        v.setAnoFabrico(r.anoFabrico());
        v.setStatusVeiculo(r.statusVeiculo() != null ? r.statusVeiculo() : StatusVeiculo.ATIVO);
        v.setQuilometragem(r.quilometragem() != null ? r.quilometragem() : 0);
    }
}