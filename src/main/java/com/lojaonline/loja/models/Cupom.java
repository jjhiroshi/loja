package com.lojaonline.loja.models;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Table( name = "cupons")
@Entity( name = "cupom")
public class Cupom {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "cupom_id")
    private Long id;

    @Column(nullable = false)
    private String codigo;

    @Column (nullable = false)
    private Double desconto;

    @Enumerated(EnumType.STRING)
    private StatusCupom StatusCupom;

    @Enumerated(EnumType.STRING)
    private FinalidadeCupom finalidade;

    // Relacionamento para outras classes:
    @OneToMany (cascade =  CascadeType.ALL, mappedBy = "cupom")
    private List<Pedido> pedidos;

    // construtor padrao
    public Cupom() {
    }

    // construtor sem campo id
    public Cupom(String codigo, Double desconto, StatusCupom statusCupom,
            FinalidadeCupom finalidade, List<Pedido> pedidos) {
        this.codigo = codigo;
        this.desconto = desconto;
        StatusCupom = statusCupom;
        this.finalidade = finalidade;
        this.pedidos = pedidos;
    }

    // Gets e Sets
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Double getDesconto() {
        return desconto;
    }

    public void setDesconto(Double desconto) {
        this.desconto = desconto;
    }

    public StatusCupom getStatusCupom() {
        return StatusCupom;
    }

    public void setStatusCupom(StatusCupom statusCupom) {
        StatusCupom = statusCupom;
    }

    public FinalidadeCupom getFinalidade() {
        return finalidade;
    }

    public void setFinalidade(FinalidadeCupom finalidade) {
        this.finalidade = finalidade;
    }

    public List<Pedido> getPedidos() {
        return pedidos;
    }

    public void setPedidos(List<Pedido> pedidos) {
        this.pedidos = pedidos;
    }   
}
