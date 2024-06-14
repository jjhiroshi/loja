package com.lojaonline.loja.models;


import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Table( name = "pedidos")
@Entity (name = "pedido")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "pedido_id")
    private Long id;

    @Column
    private Date dataDoPedido;

    @Column
    @Enumerated(EnumType.STRING)
    private Situacao situacao;

    @Column
    private Double frete;

    @Column
    private Double total;

    // Relacionamento com outras classes:

    @ManyToOne
    @JoinColumn ( name = "fk_cliente_id")
    private Cliente cliente;

    @ManyToOne
    @JoinColumn ( name = "fk_endereco_id")
    private Endereco enderecoDeEntrega;

    @OneToMany (cascade = CascadeType.ALL, mappedBy = "pedido")
    private Set<PedidoJogo> pedidosJogos = new HashSet<>();

    @OneToMany (cascade = CascadeType.ALL, mappedBy = "pedido")
    private Set<PedidoCartao> pedidosCartoes = new HashSet<>();
    
    @ManyToOne
    @JoinColumn ( name = "cupom_id")
    private Cupom cupom;

    
    // Default Construtor
    public Pedido(){}

    // Constructor without ID
    public Pedido(Date dataDoPedido, Situacao situacao, Double frete, Double total, Cliente cliente,
            Endereco enderecoDeEntrega, Set<PedidoJogo> pedidosJogos, Set<PedidoCartao> pedidosCartoes, Cupom cupom) {
        this.dataDoPedido = dataDoPedido;
        this.situacao = situacao;
        this.frete = frete;
        this.total = total;
        this.cliente = cliente;
        this.enderecoDeEntrega = enderecoDeEntrega;
        this.pedidosJogos = pedidosJogos;
        this.pedidosCartoes = pedidosCartoes;
        this.cupom = cupom;
    }


    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDataDoPedido() {
        return dataDoPedido;
    }

    public void setDataDoPedido(Date dataDoPedido) {
        this.dataDoPedido = dataDoPedido;
    }

    public Situacao getSituacao() {
        return situacao;
    }

    public void setSituacao(Situacao situacao) {
        this.situacao = situacao;
    }

    public Double getFrete() {
        return frete;
    }

    public void setFrete(Double frete) {
        this.frete = frete;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Endereco getEnderecoDeEntrega() {
        return enderecoDeEntrega;
    }

    public void setEnderecoDeEntrega(Endereco enderecoDeEntrega) {
        this.enderecoDeEntrega = enderecoDeEntrega;
    }

    public Set<PedidoJogo> getPedidosJogos() {
        return pedidosJogos;
    }

    public void setPedidosJogos(Set<PedidoJogo> pedidosJogos) {
        this.pedidosJogos = pedidosJogos;
    }

    public Set<PedidoCartao> getPedidosCartoes() {
        return pedidosCartoes;
    }

    public void setPedidosCartoes(Set<PedidoCartao> pedidosCartoes) {
        this.pedidosCartoes = pedidosCartoes;
    }

    public Cupom getCupom() {
        return cupom;
    }

    public void setCupom(Cupom cupom) {
        this.cupom = cupom;
    }
}
