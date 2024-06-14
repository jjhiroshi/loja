package com.lojaonline.loja.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Table (name = "pedidos_jogos")
@Entity (name = "pedido_jogo")
public class PedidoJogo {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn (name = "fk_pedido_id")
    private Pedido pedido;

    @ManyToOne
    @JoinColumn (name = "fk_jogo_id")
    private Jogo jogo;

    @Column (nullable = false)
    private Integer quantidade;

    @Column (nullable =  false)
    private Double subtotal;

    // Constructors:
    public PedidoJogo() {
    }

    public PedidoJogo(Pedido pedido, Jogo jogo, Integer quantidade, Double subtotal) {
        this.pedido = pedido;
        this.jogo = jogo;
        this.quantidade = quantidade;
        this.subtotal = subtotal;
    }

    // Getters and Setters:


    
    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public Jogo getJogo() {
        return jogo;
    }

    public void setJogo(Jogo jogo) {
        this.jogo = jogo;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public Double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
