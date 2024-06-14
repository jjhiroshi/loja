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


import com.lojaonline.loja.dto.RequisicaoFormCartao;
import com.lojaonline.loja.dto.RequisicaoFormCliente;
import com.lojaonline.loja.dto.RequisicaoFormEndereco;
import com.lojaonline.loja.models.Cartao;
import com.lojaonline.loja.models.Cliente;
import com.lojaonline.loja.models.Endereco;
import com.lojaonline.loja.repositories.CartaoRepository;
import com.lojaonline.loja.repositories.ClienteRepository;
import com.lojaonline.loja.repositories.EnderecoRepository;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;




@Controller
@RequestMapping("/clientes")
public class ClienteController {
    
    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private CartaoRepository cartaoRepository;
        
    /* ******************** CRUD DO ADMINISTRADOR QUANTO AO CADASTRO DO CLIENTE ************************ */

    // Listar os clientes cadastrados para o administrador
    @GetMapping("")
    public ModelAndView listarCliente() {
        List<Cliente> clientes = this.clienteRepository.findAll();
        ModelAndView view = new ModelAndView("clientes/listarClientes");
        view.addObject("clientes", clientes);
        return view;
    }
    
    // Admin
    @GetMapping("/{id}")
    public ModelAndView exibirCliente(@PathVariable Long id){

        ModelAndView view = new ModelAndView("/clientes/detalhes");

        // busca cliente no banco de dados
        Optional<Cliente> opcional = this.clienteRepository.findById(id);
        
        if(opcional.isPresent()){
            // Quando acha esse cliente no banco de dados:

            // recupera cliente encontrado no banco de dados
            Cliente cliente = opcional.get();
            
            view.addObject("cliente", cliente);
            view.addObject("enderecos", cliente.getEnderecos());
            
            
            return view;
        }else{
            // Não achou nenhum registro no banco de dados com esse ID
            System.out.println("******* NÃO ACHOU CLIENTE DE ID: " + id + " *********");
            return new ModelAndView("index");
        }
    }

    @GetMapping("/{id}/excluir")
    public ModelAndView excluir(@PathVariable Long id){
        ModelAndView view = new ModelAndView("redirect:/clientes");
        try{
            this.clienteRepository.deleteById(id);
            return view;
        }catch (EmptyResultDataAccessException e){
            System.out.println("Exceção Ocorrida: " + e);
            return view;
        }
        
    }
    /* ******************** FIM DO ACESSO DO ADMINISTRADOR QUANTO AO CADASTRO DO CLIENTE ************************ */
    
    
    /* ******************** CRUD DO CADASTRO DO CLIENTE ************************ */

    // Cliente se cadastra na loja
    @GetMapping("/novoCliente")
    public ModelAndView novoClienteSeCadastra( RequisicaoFormCliente requisicaoCliente, HttpSession sessao){
        ModelAndView view = new ModelAndView("/clientes/novoCliente");
        view.addObject("requisicaoCliente", requisicaoCliente);
        return view;
    }
    
    
    @PostMapping("/novoCliente")
    public ModelAndView cadastrarCliente (@Valid RequisicaoFormCliente requisicaoCliente, RequisicaoFormEndereco requisicaoEndereco, BindingResult bindingResult, HttpSession sessao){
        // Se tiver erro no formulário
        if(bindingResult.hasErrors()){
            System.out.println("Tem erros no formulário");
            ModelAndView view = new ModelAndView("/clientes/novoCliente");
            view.addObject("requisicaoCliente", requisicaoCliente);
            
            return view;
        }
        
        // Pegar os dados do formulário
        Cliente cliente = requisicaoCliente.formularioToCliente();
        
        // Salvar cliente na sessão
        sessao.setAttribute("cliente", cliente);
        // Salvar cliente no Banco de Dados
        this.clienteRepository.save(cliente);

        // Prosseguir para a próxima página que é o cadastro do endereço
        ModelAndView view = new ModelAndView("redirect:/clientes/" + cliente.getId() + "/novoEndereco");
        view.addObject("requisicaoEndereco", requisicaoEndereco);
        return view;
    }

    @GetMapping("/{id}/novoEndereco")
    public ModelAndView novoEndereco (@PathVariable Long id, RequisicaoFormEndereco requisicaoEndereco, HttpSession sessao){
        ModelAndView view = new ModelAndView("/clientes/novoEndereco");
        view.addObject("requisicaoEndereco", requisicaoEndereco);
        return view;
    }


    @PostMapping("/{id}/novoEndereco")
    public ModelAndView cadastrarEndereco (@PathVariable Long id, @Valid RequisicaoFormEndereco requisicaoEndereco, BindingResult bindingResult, HttpSession sessao){
        // Se tiver erro no formulário
        if(bindingResult.hasErrors()){
            System.out.println("Tem erros no formulário");
            ModelAndView view = new ModelAndView("/clientes/novoEndereco");
            view.addObject("requisicaoEndereco", requisicaoEndereco);
            
            return view;
        }
        Cliente cliente = new Cliente();

        cliente.setId(id);

        // Pegar dados de endereço do formulario
        Endereco endereco = requisicaoEndereco.formularioToEndereco();

        
        // salvar cliente no endereço
        endereco.setCliente(cliente);

        // salvar cliente no Banco de Dados
        this.enderecoRepository.save(endereco);

        // Prosseguir para a próxima página que é a exibição dos dados cadastrados
        ModelAndView view = new ModelAndView("redirect:/clientes/cliente/" + id);
        
        return view;
    }

    @GetMapping("/cliente/{id}")
    public ModelAndView exibirDetalhesDoCliente(@PathVariable Long id, RequisicaoFormCartao requisicaoCartao, HttpSession sessao){

        ModelAndView view = new ModelAndView("/clientes/exibirCliente");

        // busca cliente no banco de dados
        Optional<Cliente> opcional = this.clienteRepository.findById(id);
        
        if(opcional.isPresent()){
            // Quando acha esse cliente no banco de dados:

            // recupera cliente encontrado no banco de dados
            Cliente cliente = opcional.get();
            
            view.addObject("cliente", cliente);
            view.addObject("enderecos", cliente.getEnderecos());
            
            view.addObject("requisicaoCartao", requisicaoCartao);

            List<Cartao> cartoes = this.cartaoRepository.findByCliente(cliente);
            if(!cartoes.isEmpty()){ // se tiver algum cartão salvo no banco de dados:
                view.addObject("cartoes", cartoes);
            }
            sessao.invalidate();
            return view;
        }else{
            // Não achou nenhum registro no banco de dados com esse ID
            System.out.println("******* NÃO ACHOU CLIENTE DE ID: " + id + " *********");
            sessao.invalidate();
            return new ModelAndView("index");
        }
    }

    @PostMapping ("/{id}/novoCartao")
    public ModelAndView cadastrarCartao (@PathVariable Long id, RequisicaoFormCartao cartaoDTO){
        ModelAndView view = new ModelAndView("/clientes/exibirCliente");

        Optional<Cliente> resultado = this.clienteRepository.findById(id);
        Cliente cliente = resultado.get();
        cartaoDTO.setCliente(cliente);
        Cartao cartao = cartaoDTO.toCartao();

        this.cartaoRepository.save(cartao);

        view.addObject("cliente", cliente);
        view.addObject("enderecos", cliente.getEnderecos());

        List<Cartao> cartoes = this.cartaoRepository.findByCliente(cliente);
        view.addObject("cartoes", cartoes);

        return view;
    }
    

    @GetMapping("/cancelarCadastro")
    public ModelAndView cancelarCadastro (HttpSession sessao){
        sessao.invalidate();
        return new ModelAndView("redirect:/home");
    }
    
    @PostMapping("/buscar")
    public ModelAndView buscarCliente(RequisicaoFormCliente requisicao){
        
        List<Cliente> resultado = this.clienteRepository.findByCpf(requisicao.getCpf());
        

        if(resultado.isEmpty()){
            // Não achou nenhum registro no banco de dados com esse CPF
            System.out.println("******* NÃO ACHOU CLIENTE COM ESSE CPF: " + requisicao.getCpf() + " *********");
            return new ModelAndView("redirect:/home");
            
        }else{
            
            Cliente cliente = resultado.get(0);

            ModelAndView view = new ModelAndView("clientes/exibirCliente");
            view.addObject("cliente", cliente);
            view.addObject("enderecos", cliente.getEnderecos());
            view.addObject("cartoes", cliente.getCartoes());

            return view;
        }
    }
    
    // Aqui faremos a busca pelo cliente e montar a página para edição dos dados pessoais
    @GetMapping("/{id}/editarInfo")
    public ModelAndView editar(@PathVariable Long id, RequisicaoFormCliente requisicaoCliente){
        Optional<Cliente> opcional = this.clienteRepository.findById(id);

        if(opcional.isPresent()){
            // achou um registro com esse ID
            Cliente cliente = opcional.get();
            
            ModelAndView view = new ModelAndView("clientes/editarInfo");
            
            requisicaoCliente.fromCliente(cliente);
            view.addObject("requisicaoCliente", requisicaoCliente);
            view.addObject("cliente", cliente);
            return view;
        }else{
            // Não achou registro com esse ID
            System.out.println("****** Não achou cliente com esse ID" + id + " *******");
            return new ModelAndView("index");
        }
    }

    @PostMapping("/{id}/atualizarInfo")
    public ModelAndView atualizarInfo (@PathVariable Long id, @Valid RequisicaoFormCliente requisicao, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            // se tiver erros no formulário
            
            System.out.println("Tem erros no formulário");
            ModelAndView view = new ModelAndView("clientes/editarInfo");
            view.addObject("requisicaoCliente", requisicao);
            return view;
        
        }else{
            // se todos os campos estiverem preenchidos no formulário
            
            // Procura no banco de dados um cliente com determinado Id
            Optional<Cliente> opcional = this.clienteRepository.findById(id);

            if(opcional.isPresent()){
                // achou o registro no banco
                Cliente cliente = opcional.get();
                cliente.setNome(requisicao.getNome());
                cliente.setCpf(requisicao.getCpf());
                cliente.setEmail(requisicao.getEmail());
                
                // salva no banco com os dados atualizados retirados do form
                this.clienteRepository.save(cliente);

                return new ModelAndView("redirect:/clientes/cliente/" + cliente.getId());
            } else {
                // não achou o registro no banco
                System.out.println("*** Não achou um cliente com o iD" + id + " ****");
                return new ModelAndView("index");
            }
        }
    }

    // Aqui faremos a busca pelo ID do Endereço e montar a página para edição dos dados
    @GetMapping("/{id}/editarEndereco")
    public ModelAndView editarEndereco (@PathVariable Long id, RequisicaoFormEndereco requisicaoEndereco){
        Optional<Endereco> opcional = this.enderecoRepository.findById(id);

        if(opcional.isPresent()){
            // achou um registro com esse ID
            Endereco endereco = opcional.get();
            
            ModelAndView view = new ModelAndView("clientes/editarEndereco");
            
            requisicaoEndereco.fromEndereco(endereco);
            view.addObject("requisicaoEndereco", requisicaoEndereco);
            view.addObject("endereco", endereco);
            return view;
        }else{
            // Não achou registro com esse ID
            System.out.println("****** Não achou Endereço com esse ID" + id + " *******");
            return new ModelAndView("index");
        }
    }

    @PostMapping("/{id}/atualizarEndereco")
    public ModelAndView atualizarEndereco (@PathVariable Long id, @Valid RequisicaoFormEndereco requisicao, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            // se tiver erros no formulário
            
            System.out.println("Tem erros no formulário");
            ModelAndView view = new ModelAndView("clientes/editarEndereco");
            view.addObject("requisicaoEndereco", requisicao);
            return view;
        
        }else{
            // se todos os campos estiverem preenchidos no formulário
            
            // Procura no banco de dados um cliente com determinado Id
            Optional<Endereco> opcional = this.enderecoRepository.findById(id);

            if(opcional.isPresent()){
                // achou o registro no banco
                Endereco endereco = opcional.get();
                endereco.setLogradouro(requisicao.getLogradouro());
                endereco.setNumero(requisicao.getNumero());
                endereco.setComplemento(requisicao.getComplemento());
                endereco.setBairro(requisicao.getBairro());
                endereco.setCidade(requisicao.getCidade());
                endereco.setUf(requisicao.getUf());
                endereco.setCep(requisicao.getCep());

                
                // salva no banco com os dados atualizados retirados do form
                this.enderecoRepository.save(endereco);

                return new ModelAndView("redirect:/clientes/cliente/" + endereco.getCliente().getId());
            } else {
                // não achou o registro no banco
                System.out.println("*** Não achou um cliente com o iD" + id + " ****");
                return new ModelAndView("index");
            }
        }
    }
}