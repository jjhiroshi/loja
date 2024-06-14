package com.lojaonline.loja.dto;

import com.lojaonline.loja.models.Cartao;
import com.lojaonline.loja.models.Cliente;

import jakarta.validation.constraints.NotEmpty;

public class RequisicaoFormCartao {

    @NotEmpty
    private String numero;

    @NotEmpty
    private String validade;

    private Cliente cliente;

    private Double valor;

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public String getNumero() {
        return this.numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getValidade() {
        return this.validade;
    }

    public void setValidade(String validade) {
        this.validade = validade;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public Cartao toCartao(){
        Cartao cartao = new Cartao();
        cartao.setNumero(this.numero);
        cartao.setValidade(this.validade);
        cartao.setCliente(this.cliente);
        return cartao;
    }

    public void fromCartao (Cartao cartao){
        this.numero = cartao.getNumero();
        this.validade = cartao.getValidade();
    }

}
