package com.lojaonline.loja.models;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Table(name = "cartoes")
@Entity(name = "cartao")
public class Cartao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "cartao_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String numero;
    
    @Column(nullable = false)
    private String validade;
    
    // Relacionamentos com outras classes:

    @ManyToOne
    @JoinColumn (name = "cliente_id")
    private Cliente cliente;

    @OneToMany (cascade = CascadeType.ALL, mappedBy = "cartao")
    private Set<PedidoCartao> pedidosCartoes = new HashSet<>();


    // construtor padrão
    public Cartao(){}

    // construtor sem id
    public Cartao(String numero, String validade, Cliente cliente, Set<PedidoCartao> pedidosCartoes) {
        this.numero = numero;
        this.validade = validade;
        this.cliente = cliente;
        this.pedidosCartoes = pedidosCartoes;
    }

    // Gets e Sets
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getValidade() {
        return validade;
    }

    public void setValidade(String validade) {
        this.validade = validade;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Set<PedidoCartao> getPedidosCartoes() {
        return pedidosCartoes;
    }

    public void setPedidosCartoes(Set<PedidoCartao> pedidosCartoes) {
        this.pedidosCartoes = pedidosCartoes;
    }

}
