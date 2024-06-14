package com.lojaonline.loja.models;

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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Table(name = "jogos")
@Entity(name = "jogo")
public class Jogo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "jogo_id")
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Enumerated(EnumType.STRING)
    private Categoria categoria;

    @Column(nullable = false)
    private String descricao;
    
    @Column (nullable = true)
    private Double preco;

    @Column (nullable = false)
    private String urlFotoPrincipal;

    @Column (nullable = true)
    private String urlFoto1;

    @Column (nullable = true)
    private String urlFoto2;

    @OneToMany (cascade = CascadeType.ALL, mappedBy = "jogo")
    private Set<PedidoJogo> pedidosJogos = new HashSet<>();
    
    // Default Constructor
    public Jogo(){}

    // Constructor without ID
    public Jogo(String nome, Categoria categoria, String descricao, Double preco, String urlFotoPrincipal,
            String urlFoto1, String urlFoto2, Set<PedidoJogo> pedidosJogos) {
        this.nome = nome;
        this.categoria = categoria;
        this.descricao = descricao;
        this.preco = preco;
        this.urlFotoPrincipal = urlFotoPrincipal;
        this.urlFoto1 = urlFoto1;
        this.urlFoto2 = urlFoto2;
        this.pedidosJogos = pedidosJogos;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }

    public String getUrlFotoPrincipal() {
        return urlFotoPrincipal;
    }

    public void setUrlFotoPrincipal(String urlFotoPrincipal) {
        this.urlFotoPrincipal = urlFotoPrincipal;
    }

    public String getUrlFoto1() {
        return urlFoto1;
    }

    public void setUrlFoto1(String urlFoto1) {
        this.urlFoto1 = urlFoto1;
    }

    public String getUrlFoto2() {
        return urlFoto2;
    }

    public void setUrlFoto2(String urlFoto2) {
        this.urlFoto2 = urlFoto2;
    }

    public Set<PedidoJogo> getPedidosJogos() {
        return pedidosJogos;
    }

    public void setPedidosJogos(Set<PedidoJogo> pedidosJogos) {
        this.pedidosJogos = pedidosJogos;
    }
}
