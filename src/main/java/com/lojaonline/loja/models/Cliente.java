package com.lojaonline.loja.models;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Table(name = "clientes")
@Entity(name = "cliente")
public class Cliente{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name ="cliente_id")
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String cpf;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String telefone;

    // Relacionamentos com outras classes:
    
    @OneToMany (cascade = CascadeType.ALL, mappedBy = "cliente")
    private List<Endereco> enderecos;
    
    @OneToMany (cascade = CascadeType.ALL, mappedBy = "cliente")
    private List<Pedido> pedidos;

    @OneToMany (cascade = CascadeType.ALL, mappedBy = "cliente")
    private Set<Cartao> cartoes = new HashSet<>();
    
    // construtor padrão
    public Cliente() {}

    // construtor sem id
    public Cliente(String nome, String cpf, String email, String telefone, List<Endereco> enderecos, List<Pedido> pedidos, Set<Cartao> cartoes) {
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
        this.telefone = telefone;
        this.enderecos = enderecos;
        this.pedidos = pedidos;
        this.cartoes = cartoes;
    }

    // Gets e Sets
    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return this.nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return this.cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return this.telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public List<Endereco> getEnderecos() {
        return this.enderecos;
    }

    public void setEnderecos(List<Endereco> enderecos) {
        this.enderecos = enderecos;
    }

    public List<Pedido> getPedidos() {
        return this.pedidos;
    }

    public void setPedidos(List<Pedido> pedidos) {
        this.pedidos = pedidos;
    }

    public Set<Cartao> getCartoes() {
        return this.cartoes;
    }

    public void setCartoes(Set<Cartao> cartoes) {
        this.cartoes = cartoes;
    }
}
