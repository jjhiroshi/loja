package com.lojaonline.loja.dto;

import com.lojaonline.loja.models.Categoria;
import com.lojaonline.loja.models.Jogo;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;

public class RequisicaoFormJogo {

    @NotEmpty
    private String nome;

    @Enumerated(EnumType.STRING)
    private Categoria categoria;

    @NotEmpty
    private String descricao;

    @Min(1)
    private Double preco;

    @NotEmpty
    private String urlFotoPrincipal;

    private String urlFoto1;

    private String urlFoto2;

    private Integer quantidade;


    public Jogo toJogo(){
        Jogo jogo = new Jogo();
        jogo.setNome(this.nome);
        jogo.setDescricao(this.descricao);
        jogo.setCategoria(this.categoria);
        jogo.setPreco(this.preco);
        jogo.setUrlFotoPrincipal(this.urlFotoPrincipal);
        jogo.setUrlFoto1(this.urlFoto1);
        jogo.setUrlFoto2(this.urlFoto2);
        
        return jogo;
    }

    public void fromJogo(Jogo jogo){
        this.nome = jogo.getNome();
        this.categoria = jogo.getCategoria();
        this.descricao = jogo.getDescricao();
        this.preco = jogo.getPreco();
        this.urlFotoPrincipal = jogo.getUrlFotoPrincipal();
        this.urlFoto1 = jogo.getUrlFoto1();
        this.urlFoto2 = jogo.getUrlFoto2();
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

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }    
    
    
}
