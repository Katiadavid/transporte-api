package com.transporte.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import com.transporte.model.enums.TipoPassageiro;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "passageiro")
@Getter @Setter @NoArgsConstructor
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
}