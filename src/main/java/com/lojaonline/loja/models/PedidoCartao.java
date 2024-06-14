package com.lojaonline.loja.models;



import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Table (name = "pedidos_cartoes")
@Entity (name = "pedido_cartao")
public class PedidoCartao {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn (name = "fk_pedido_id")
    private Pedido pedido;

    @ManyToOne
    @JoinColumn (name = "fk_cartao_id")
    private Cartao cartao;

    @Column (nullable = false)
    private Double valor;

    // Constructors:
    public PedidoCartao() {
    }

    public PedidoCartao(Pedido pedido, Cartao cartao, Double valor) {
        this.pedido = pedido;
        this.cartao = cartao;
        this.valor = valor;
    }

    // Getters and Setters
    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public Cartao getCartao() {
        return cartao;
    }

    public void setCartao(Cartao cartao) {
        this.cartao = cartao;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    
}
