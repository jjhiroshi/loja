package com.lojaonline.loja.dto;

import com.lojaonline.loja.models.Cupom;
import com.lojaonline.loja.models.FinalidadeCupom;
import com.lojaonline.loja.models.StatusCupom;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;

public class RequisicaoFormCupom {
    @NotEmpty
    private String codigo;

    @Min(0)
    private Double desconto;

    @Enumerated(EnumType.STRING)
    private StatusCupom statusCupom;

    @Enumerated(EnumType.STRING)
    private FinalidadeCupom finalidade;

    public Cupom toCupom(){
        Cupom cupom = new Cupom();
        cupom.setCodigo(this.codigo);
        cupom.setDesconto(this.desconto);
        cupom.setStatusCupom(this.statusCupom);
        cupom.setFinalidade(this.finalidade);
        return cupom;
    }

    public void fromCupom(Cupom cupom){
        this.codigo = cupom.getCodigo();
        this.desconto = cupom.getDesconto();
        this.statusCupom = cupom.getStatusCupom();
        this.finalidade = cupom.getFinalidade();
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
        return statusCupom;
    }

    public void setStatusCupom(StatusCupom statusCupom) {
        this.statusCupom = statusCupom;
    }

    public FinalidadeCupom getFinalidade() {
        return finalidade;
    }

    public void setFinalidade(FinalidadeCupom finalidade) {
        this.finalidade = finalidade;
    }
}

