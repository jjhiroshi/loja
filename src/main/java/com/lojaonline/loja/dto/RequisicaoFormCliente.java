package com.lojaonline.loja.dto;

import com.lojaonline.loja.models.Cliente;

import jakarta.validation.constraints.NotBlank;

// Data Transfer Object
public class RequisicaoFormCliente {
    
    @NotBlank
    private String nome;

    @NotBlank
    private String cpf;

    @NotBlank
    private String email;
    
    @NotBlank
    private String telefone;
    
    
    
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public String getCpf() {
        return cpf;
    }
    public void setCpf(String cpf) {
        this.cpf = cpf;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getTelefone() {
        return telefone;
    }
    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
    
    public Cliente formularioToCliente(){
        Cliente cliente = new Cliente();
        cliente.setNome(this.nome);
        cliente.setCpf(this.cpf);
        cliente.setEmail(this.email);
        cliente.setTelefone(this.telefone);

        return cliente;
    }

    public void fromCliente(Cliente cliente){
        this.nome = cliente.getNome();
        this.cpf = cliente.getCpf();
        this.email = cliente.getEmail();
        this.telefone = cliente.getTelefone();
    }    
    
}
