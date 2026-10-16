package com.transporte.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import com.transporte.model.enums.StatusMotorista;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "motorista")
@Getter @Setter @NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Motorista {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "IdM")
    private Integer id;

    @Column(name = "data_contratacao", nullable = false)
    private LocalDate dataContratacao;

    @Column(name = "nome", nullable = false, length = 120)
    private String nome;

    @Column(name = "carta_conducao", nullable = false, length = 30, unique = true)
    private String cartaDeConducao;

    @Column(name = "data_validade_carta", nullable = false)
    private LocalDate dataValidadeCarta;

    @Column(name = "telefone", nullable = false, length = 20)
    private String telefone;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_motorista", nullable = false)
    private StatusMotorista statusMotorista = StatusMotorista.ATIVO;

    @ElementCollection
    @CollectionTable(name = "motorista_endereco", joinColumns = @JoinColumn(name = "IdM"))
    @Column(name = "endereco", length = 200)
    private Set<String> enderecos = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "motorista_veiculo",
            joinColumns = @JoinColumn(name = "IdM"),
            inverseJoinColumns = @JoinColumn(name = "matricula"))
    private Set<Veiculo> veiculos = new HashSet<>();
}