package com.transporte.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import com.transporte.model.enums.TipoPassageiro;

import jakarta.persistence.*;

@Entity
@Table(name = "passageiro")
public class Passageiro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdP")
    private Integer id;

    @Column(name = "nuit", nullable = false, length = 9, unique = true)
    private String nuit;

    @Column(name = "nome", nullable = false, length = 120)
    private String nome;

    @Column(name = "telefone", nullable = false, length = 20)
    private String telefone;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_passageiro", nullable = false)
    private TipoPassageiro tipoPassageiro;

    @Column(name = "data_nascimento", nullable = false)
    private LocalDate dataNascimento;

    @ElementCollection
    @CollectionTable(name = "passageiro_endereco", joinColumns = @JoinColumn(name = "IdP"))
    @Column(name = "endereco", length = 200)
    private Set<String> enderecos = new HashSet<>();

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getNuit() { return nuit; }
    public void setNuit(String nuit) { this.nuit = nuit; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    public TipoPassageiro getTipoPassageiro() { return tipoPassageiro; }
    public void setTipoPassageiro(TipoPassageiro tipoPassageiro) { this.tipoPassageiro = tipoPassageiro; }
    public LocalDate getDataNascimento() { return dataNascimento; }
    public void setDataNascimento(LocalDate dataNascimento) { this.dataNascimento = dataNascimento; }
    public Set<String> getEnderecos() { return enderecos; }
    public void setEnderecos(Set<String> enderecos) { this.enderecos = enderecos; }
}