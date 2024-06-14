package com.lojaonline.loja.models;

import java.util.HashSet;
import java.util.Set;

public class Carrinho {

    private Long id;

    private Set<PedidoJogo> jogosNoCarrinho = new HashSet<>();

    private Double total;

    // construtor vazio
    public Carrinho() {}

    // construtor com atributos, menos ID
    public Carrinho(Set<PedidoJogo> jogosNoCarrinho, Double total) {
        this.jogosNoCarrinho = jogosNoCarrinho;
        this.total = total;
    }

    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }

    public Set<PedidoJogo> getJogosNoCarrinho() {
        return jogosNoCarrinho;
    }

    public void setJogosNoCarrinho(Set<PedidoJogo> jogosNoCarrinho) {
        this.jogosNoCarrinho = jogosNoCarrinho;
    }

    public Double getTotal() {
        return total;
    }


    public void setTotal(Double total) {
        this.total = total;
    }

    public Double calcularPrecoXQtde (Jogo jogo, Integer quantidade){
        return (jogo.getPreco() * quantidade);
    }

    
    public Double calculaSubTotal (Set<PedidoJogo> jogosNoCarrinho ){
        Double subTotal = 0.0;

        for(PedidoJogo jogo : jogosNoCarrinho){
            subTotal += jogo.getSubtotal();
        }
        return subTotal;
    }
    

    public boolean ehJogoDiferente (Long id, Set<PedidoJogo> jogos){
        boolean flag = true;

         for(PedidoJogo jogo : jogos){
            if(jogo.getJogo().getId() == id){
                return flag = false; // é o mesmo jogo
            }
         }
         return flag; // true : é um jogo diferente
    }

    
    

}
