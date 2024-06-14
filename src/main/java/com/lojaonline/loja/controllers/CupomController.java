package com.lojaonline.loja.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.lojaonline.loja.dto.RequisicaoFormCupom;
import com.lojaonline.loja.models.Cupom;
import com.lojaonline.loja.models.FinalidadeCupom;
import com.lojaonline.loja.models.StatusCupom;
import com.lojaonline.loja.repositories.CupomRepository;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/cupons")
public class CupomController {

    @Autowired
    private CupomRepository cupomRepository;

    @GetMapping("")
    public ModelAndView listar(){
        ModelAndView view = new ModelAndView("/cupons/listarCupons");
        List<Cupom> cupons = this.cupomRepository.findAll();
        view.addObject("cupons", cupons);
        return view;
    }

    @GetMapping("/novo")
    public ModelAndView montarFormNovoCupom (RequisicaoFormCupom formularioCupom){
        ModelAndView view = new ModelAndView("/cupons/criarCupom");
        view.addObject("formularioCupom", formularioCupom);
        view.addObject("statusCupom", StatusCupom.values());
        view.addObject("finalidadeCupom", FinalidadeCupom.values());
        return view;
    }

    @PostMapping("/novo")
    public ModelAndView criar (@Valid RequisicaoFormCupom formularioCupom, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            ModelAndView view= new ModelAndView("/cupons/criarCupom");
            view.addObject("formularioCupom", formularioCupom);
            return view;
        }else{
            Cupom cupom = formularioCupom.toCupom();
            this.cupomRepository.save(cupom);
            return new ModelAndView("redirect:/cupons/" + cupom.getId());
        }
    }

    @GetMapping("/{id}")
    public ModelAndView exibir(@PathVariable Long id){
        Optional<Cupom> resultado = this.cupomRepository.findById(id);

        if(resultado.isPresent()){
            // Achou um cupom com esse ID

            // Atribui num objeto Cupom os dados do banco de dados
            Cupom cupom = resultado.get();

            // Prepara o view que será montado após esse método
            ModelAndView view = new ModelAndView("/cupons/exibirCupom");

            // adiciona o objeto Cupom que será montado na view
            view.addObject("cupom", cupom);

            return view;
        }else{
            // Não achou cupom com esse ID
            return new ModelAndView("/cupons");
        }

    }


    @GetMapping("/{id}/excluir")
    public ModelAndView excluir (@PathVariable Long id){
        ModelAndView view = new ModelAndView("redirect:/cupons");
        try{
            this.cupomRepository.deleteById(id);
            return view;
        }catch (EmptyResultDataAccessException e ){
            System.out.println("Exceção ocorrida: " + e);
            return view;
        }
    }
    
    @GetMapping("/{id}/editar")
    public ModelAndView buscaCupomMontaForm (@PathVariable Long id, RequisicaoFormCupom formularioCupom){
        Optional<Cupom> resultado = this.cupomRepository.findById(id);

        if(resultado.isPresent()){
            // Achou um cupom no banco de dados com esse ID

            // Atribui o resultado num obejto Cupom
            Cupom cupom = resultado.get();

            // escolhe o arquivo html que vai montar depois desse método
            ModelAndView view = new ModelAndView("/cupons/editarCupom");

            // atribui na requisicao os dados do cupom encontrado no banco de dados
            formularioCupom.fromCupom(cupom);

            view.addObject("formularioCupom", formularioCupom);
            view.addObject("cupomId", cupom.getId());
            view.addObject("statusCupom", StatusCupom.values());
            view.addObject("finalidadeCupom", FinalidadeCupom.values());
            return view;
        }else{
            // Não achou cupom com esse ID
            return new ModelAndView("redirect:/cupons");
        }
    }

    @PostMapping("{id}")
    public ModelAndView editar(@PathVariable Long id, @Valid RequisicaoFormCupom formularioCupom, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            // se no formulário tiver erros

            System.out.println("Tem erros no formulário de editar cupom");

            // montar a página para 
            ModelAndView view = new ModelAndView("cupons/" + id + "/editar");

            // devolve no form os dados já preenchidos no form
            view.addObject("formularioCupom", formularioCupom);

            return view;
        }else{
            // Todos os campos estão devidamente preenchidos

            // Então vamos procurar no banco de dados um cupom com esse ID
            Optional<Cupom> resultado = this.cupomRepository.findById(id);

            if(resultado.isPresent()){
                // acho no banco de dados um cupom com esse ID, pois só assim o
                // Spring realiza a ação Update pois tem um registro com esse ID
                // se não tivesse um registro com esse ID, ele salvaria como um novo cupom

                // Atribui no objeto Cupom os dados encontrados no BD com esse ID
                Cupom cupom = resultado.get();

                // Tirando ID atribui no objeto os dados atualizados no Form
                cupom.setCodigo(formularioCupom.getCodigo());
                cupom.setDesconto(formularioCupom.getDesconto());
                cupom.setStatusCupom(formularioCupom.getStatusCupom());
                cupom.setFinalidade(formularioCupom.getFinalidade());

                // Atualiza no BD esse cupom
                this.cupomRepository.save(cupom);

                return new ModelAndView("redirect:/cupons/" + cupom.getId());
            }else{
                // Não achou um cupom com esse ID
                return new ModelAndView("redirect:/cupons");

            }

        }

    }
}
