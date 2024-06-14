package com.lojaonline.loja.dto;

import com.lojaonline.loja.models.Cliente;
import com.lojaonline.loja.models.Endereco;

import jakarta.validation.constraints.NotBlank;

// DTO de Endereço
public class RequisicaoFormEndereco {

    @NotBlank
    private String logradouro;

    @NotBlank
    private String numero;
    
    
    private String complemento;

    @NotBlank
    private String bairro;

    @NotBlank
    private String cidade;

    
    private String uf;

    @NotBlank
    private String cep;

    private Cliente cliente;

    // Gets e Sets
    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public Endereco formularioToEndereco(){
        Endereco endereco = new Endereco();
        endereco.setLogradouro(this.logradouro);
        endereco.setNumero(this.numero);
        endereco.setComplemento(this.complemento);
        endereco.setBairro(this.bairro);
        endereco.setCidade(this.cidade);
        endereco.setUf(this.uf);
        endereco.setCep(this.cep);
        return endereco;
    }

    public void fromEndereco (Endereco end){
        this.logradouro = end.getLogradouro();
        this.numero = end.getNumero();
        this.complemento = end.getComplemento();
        this.bairro = end.getBairro();
        this.cidade = end.getCidade();
        this.uf = end.getUf();
        this.cep = end.getCep();
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    
    
}
