package com.transporte.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import com.transporte.model.enums.StatusMotorista;

import jakarta.persistence.*;

@Entity
@Table(name = "motorista")
public class Motorista {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public LocalDate getDataContratacao() { return dataContratacao; }
    public void setDataContratacao(LocalDate dataContratacao) { this.dataContratacao = dataContratacao; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getCartaDeConducao() { return cartaDeConducao; }
    public void setCartaDeConducao(String cartaDeConducao) { this.cartaDeConducao = cartaDeConducao; }
    public LocalDate getDataValidadeCarta() { return dataValidadeCarta; }
    public void setDataValidadeCarta(LocalDate dataValidadeCarta) { this.dataValidadeCarta = dataValidadeCarta; }
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    public StatusMotorista getStatusMotorista() { return statusMotorista; }
    public void setStatusMotorista(StatusMotorista statusMotorista) { this.statusMotorista = statusMotorista; }
    public Set<String> getEnderecos() { return enderecos; }
    public void setEnderecos(Set<String> enderecos) { this.enderecos = enderecos; }
    public Set<Veiculo> getVeiculos() { return veiculos; }
    public void setVeiculos(Set<Veiculo> veiculos) { this.veiculos = veiculos; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Motorista other)) return false;
        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return 31; // estável mesmo antes de o id ser gerado
    }
}