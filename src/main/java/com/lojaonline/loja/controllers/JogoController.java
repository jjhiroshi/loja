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

import com.lojaonline.loja.dto.RequisicaoFormJogo;
import com.lojaonline.loja.models.Categoria;
import com.lojaonline.loja.models.Jogo;
import com.lojaonline.loja.repositories.JogoRepository;

import jakarta.validation.Valid;


@Controller
@RequestMapping("/jogos")
public class JogoController {
    
    @Autowired
    private JogoRepository jogoRepository;

    @GetMapping("")
    public ModelAndView listar(){
        List<Jogo> jogos = this.jogoRepository.findAll();
        ModelAndView view = new ModelAndView("jogos/listarJogos");
        view.addObject("jogos", jogos);
        return view;
    }
    
    @GetMapping("/novo")
    public ModelAndView novo(RequisicaoFormJogo requisicao) {
        ModelAndView view = new ModelAndView("jogos/novoJogo");
        view.addObject("categoria", Categoria.values());
        view.addObject("requisicao", requisicao);
        return view;
    }
    
    @PostMapping("/novo")
    public ModelAndView novo (@Valid RequisicaoFormJogo requisicao, BindingResult bindingResult){
        
        if(bindingResult.hasErrors()){
            System.out.println("Tem erros no formulário");
            ModelAndView view = new ModelAndView("jogos/novoJogo");
            view.addObject("requisicao", requisicao);
            return view;
        }else{
            System.out.println(requisicao);
            Jogo jogo = requisicao.toJogo();
            this.jogoRepository.save(jogo);

            return new ModelAndView("redirect:/jogos/" + jogo.getId());
        }
    }

    @GetMapping("/{id}")
    public ModelAndView exibir(@PathVariable Long id){
        
        Optional<Jogo> opcional = this.jogoRepository.findById(id);
        
        if(opcional.isPresent()){
            Jogo jogo = opcional.get();

            ModelAndView view = new ModelAndView("jogos/exibirJogo");

            view.addObject("jogo", jogo);

            return view;
        }else{
            return new ModelAndView("redirect:/jogos");
        }
    }

    @GetMapping("/{id}/excluir")
    public ModelAndView excluir(@PathVariable Long id){
        ModelAndView view = new ModelAndView("redirect:/jogos");
        try{
            this.jogoRepository.deleteById(id);
            return view;
        }catch (EmptyResultDataAccessException e){
            System.out.println("Exceção Ocorrida: " + e);
            return view;
        }
        
    }

    // Aqui faremos a busca pelo cliente e montar a página para edição
    @GetMapping("/{id}/editar")
    public ModelAndView buscaJogoMontaFom(@PathVariable Long id, RequisicaoFormJogo requisicao){
        Optional<Jogo> opcional = this.jogoRepository.findById(id);

        if(opcional.isPresent()){
            // achou um registro com esse ID
            Jogo jogo = opcional.get();

            ModelAndView view = new ModelAndView("jogos/editarJogo");
            
            requisicao.fromJogo(jogo);
            view.addObject("requisicao", requisicao);
            view.addObject("jogoId", jogo.getId());
            view.addObject("categoria", Categoria.values());
            return view;
        }else{
            // Não achou registro com esse ID
            System.out.println("****** Não achou cliente com esse ID" + id + " *******");
            return new ModelAndView("redirect:/jogos");
        }
    }

    @PostMapping("/{id}")
    public ModelAndView atualizar(@PathVariable Long id, @Valid RequisicaoFormJogo requisicao, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            // se tiver erros no formulário
            
            System.out.println("Tem erros no formulário");
            ModelAndView view = new ModelAndView("jogos/novo");
            view.addObject("requisicao", requisicao);
            return view;
        
        }else{
            // se todos os campos estiverem preenchidos no formulário
            
            // Procura no banco de dados um cliente com determinado Id
            Optional<Jogo> opcional = this.jogoRepository.findById(id);

            if(opcional.isPresent()){
                // achou o registro no banco
                Jogo jogo = opcional.get();

                // Salva no objeto jogo os dados que m atualizados do form
                jogo.setNome(requisicao.getNome());
                jogo.setCategoria(requisicao.getCategoria());
                jogo.setDescricao(requisicao.getDescricao());
                jogo.setPreco(requisicao.getPreco());
                jogo.setUrlFotoPrincipal(requisicao.getUrlFotoPrincipal());
                jogo.setUrlFoto1(requisicao.getUrlFoto1());
                jogo.setUrlFoto2(requisicao.getUrlFoto2());
                
                // salva no banco com os dados atualizados retirados do form
                this.jogoRepository.save(jogo);

                return new ModelAndView("redirect:/jogos/" + jogo.getId());
            } else {
                // não achou o registro no banco
                System.out.println("*** Não achou um jogo com o iD" + id + " ****");
                return new ModelAndView("redirect:/jogos");
            }
            
        }
        
    }

    @PostMapping("/pesquisar")
    public ModelAndView pesquisarJogos (RequisicaoFormJogo requisicao){
       
        ModelAndView view = new ModelAndView("index");
        
        List<Jogo> jogos = this.jogoRepository.findByCategoria(requisicao.getCategoria());

        view.addObject("jogos", jogos);

        view.addObject("requisicaoJogo", requisicao);
        view.addObject("categoria", Categoria.values());
        
        return view;
    }
    
}
