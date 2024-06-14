package com.lojaonline.loja.controllers;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.lojaonline.loja.dto.RequisicaoFormCartao;
import com.lojaonline.loja.dto.RequisicaoFormCliente;
import com.lojaonline.loja.dto.RequisicaoFormEndereco;
import com.lojaonline.loja.dto.RequisicaoFormJogo;
import com.lojaonline.loja.models.Carrinho;
import com.lojaonline.loja.models.Cartao;
import com.lojaonline.loja.models.Categoria;
import com.lojaonline.loja.models.Cliente;
import com.lojaonline.loja.models.Endereco;
import com.lojaonline.loja.models.Jogo;
import com.lojaonline.loja.models.PedidoCartao;
import com.lojaonline.loja.models.PedidoJogo;
import com.lojaonline.loja.repositories.CartaoRepository;
import com.lojaonline.loja.repositories.ClienteRepository;
import com.lojaonline.loja.repositories.EnderecoRepository;
import com.lojaonline.loja.repositories.JogoRepository;

import jakarta.servlet.http.HttpSession;


@Controller
@RequestMapping("/home")
public class HomeController {

    @Autowired
    private JogoRepository jogoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private CartaoRepository cartaoRepository;
    
    @GetMapping("")
    public ModelAndView exibirPaginaPrincipal (RequisicaoFormJogo requisicao){
        // Listar no home da loja os jogos cadastrados
        List<Jogo> jogos = this.jogoRepository.findAll();

        // Carrega o arquivo da página inicial
        ModelAndView view = new ModelAndView("index.html");

        // Carrega a lista de jogos no view
        view.addObject("jogos", jogos);

        // Carrega o form para pegar a quantidade
        view.addObject("requisicaoJogo", requisicao);
        view.addObject("categoria", Categoria.values());
        
        
        return view;
    }
    
    @GetMapping("/detalhes/{id}")
    public ModelAndView exibirDetalhesDoJogo(@PathVariable Long id, RequisicaoFormJogo requisicao){
        
        Optional<Jogo> opcional = this.jogoRepository.findById(id);
        
        if(opcional.isPresent()){
            Jogo jogo = opcional.get();

            ModelAndView view = new ModelAndView("/vendas/detalhesDoJogo");

            view.addObject("jogo", jogo);
            view.addObject("requisicao", requisicao);

            return view;
        }else{
            return new ModelAndView("redirect:/home");
        }
    }
    
    
    @GetMapping("/adicionar/{idJogo}")
    public ModelAndView exibirCarrinhoDeCompras(@PathVariable Long idJogo, RequisicaoFormJogo formJogo, HttpSession sessao, RequisicaoFormCliente formCliente){

         // Carregar na view o arquivo html que vai montar
        ModelAndView view = new ModelAndView("/vendas/carrinho");
        // 
        view.addObject("formCliente", formCliente);

        // Faz a busca do jogo pelo ID
        Optional<Jogo> resultado = this.jogoRepository.findById(idJogo);

        // o objeto Jogo recebe o resultado da busca
        Jogo jogo = resultado.get();
       
        if(sessao.isNew()){
            // sim, a sessão é nova, portanto vamos salvar um objeto carrinho na sessão
            System.out.println("******* Está rodando o primeiro acesso a sessão *************");

            // Um novo carrinho
            Carrinho carrinho = new Carrinho();

            PedidoJogo novoJogo = new PedidoJogo();
            // atribui o jogo que está no carrinho
            novoJogo.setJogo(jogo);

            // Atribui a quantidade que o cliente comprará daquele jogo
            novoJogo.setQuantidade(formJogo.getQuantidade());

            // Atribui o valor que pagará pelos jogos
            // Define o preço
            Double subtotal = carrinho.calcularPrecoXQtde(jogo, novoJogo.getQuantidade());
            // Atribui no objeto jogo o valor calculado
            novoJogo.setSubtotal(subtotal);

            // Uma nova lista para receber o jogo novo
            Set<PedidoJogo> novaLista = new HashSet<>();
            novaLista.add(novoJogo);
            // Salva a lista no carrinho
            carrinho.setJogosNoCarrinho(novaLista);

            // como ainda só tem um jogo na lista então o total do carrinho é do próprio total do jogo
            carrinho.setTotal(subtotal);

            // Salva o carrinho na sessão
            sessao.setAttribute("carrinho", carrinho);

            // carrega na view a lista de carrinhos e o total
            view.addObject("jogos", carrinho.getJogosNoCarrinho());
            view.addObject("subtotal", carrinho.getTotal());
            

            return view;
        }else{
            // já havia uma sessão, vamos atualizar o carrinho
            System.out.println("******* Já temos uma sessão *************");
            
            // recuperar o carrinho armazenado na sessão
            Carrinho carrinhoSessao = (Carrinho) sessao.getAttribute("carrinho");

            // copia a lista de jogos do carrinho só para a iteração pela procura de mesmo jogo no carrinho
            Set<PedidoJogo> jogosNoCarrinho = carrinhoSessao.getJogosNoCarrinho();

            if(carrinhoSessao.ehJogoDiferente(idJogo, jogosNoCarrinho)){
                // Atribui a quantidade no objeto jogo a ser adicionado no carrinho
                //jogo.setQuantidade(requisicao.getQuantidade());
                Integer quantidade = formJogo.getQuantidade();

                // Atribui o subtotal
                Double subtotal = carrinhoSessao.calcularPrecoXQtde(jogo, quantidade);
                PedidoJogo novoJogo = new PedidoJogo();
                novoJogo.setJogo(jogo);
                novoJogo.setQuantidade(quantidade);
                novoJogo.setSubtotal(subtotal);

                // Adiciona o novo jogo no carrinho da sessão
                carrinhoSessao.getJogosNoCarrinho().add(novoJogo);

                // Calcula o Total do Carrinho
                Double subTotalAtualizado = carrinhoSessao.calculaSubTotal(carrinhoSessao.getJogosNoCarrinho());
                carrinhoSessao.setTotal(subTotalAtualizado);

                // atribui na sessão o carrinho atualizado
                sessao.setAttribute("carrinho", carrinhoSessao);

                view.addObject("jogos", carrinhoSessao.getJogosNoCarrinho());
                view.addObject("subtotal", carrinhoSessao.getTotal());
            }else{
                // Um mesmo jogo foi colocado no carrinho
                for (PedidoJogo mesmoJogo : jogosNoCarrinho){
                    // Vamos procurar esse jogo na lista
                    if(mesmoJogo.getJogo().getNome().equals(jogo.getNome())){ // é o mesmo jogo
                    
                        // Então só precisamos atualizar a quantidade e o subtotal daquele jogo
                        // Primeiro, a quantidade:
                        Integer qtde = 0;
                        // Atribui a quantidade existente + o que vem da requisicao
                        qtde = mesmoJogo.getQuantidade() + formJogo.getQuantidade();
                        // ATUALIZAREMOS A QUANTIDADE
                        mesmoJogo.setQuantidade(qtde);

                        // Segundo, o subtotal
                        // Pega o subtotal antes de acrescentar a nova quantidade
                        Double subtotal = carrinhoSessao.calcularPrecoXQtde(jogo, qtde);

                        // ATUALIZAREMOS O SUBTOTAL DO JOGO
                        mesmoJogo.setSubtotal(subtotal);

                        
                        // Atualiza o carrinho com a nova quantidade do jogo
                        carrinhoSessao.setJogosNoCarrinho(jogosNoCarrinho);
                        
                        // Calcula o Total do Carrinho
                        Double totalAtualizado = carrinhoSessao.calculaSubTotal(jogosNoCarrinho);
                        carrinhoSessao.setTotal(totalAtualizado);

                        // Atualiza a sessão com o novo carrinho
                        sessao.setAttribute("carrinho", carrinhoSessao);
                        
                        view.addObject("jogos", carrinhoSessao.getJogosNoCarrinho());
                        view.addObject("subtotal", carrinhoSessao.getTotal());

                        break;
                    }
                }// fim do for
            }// fim do else de quando já temos uma sessão
        }
        return view;
    }
    
    
    @GetMapping("/aumentar/{id}")
    public ModelAndView aumentarQtdJogo(@PathVariable Long id, RequisicaoFormJogo requisicao, HttpSession sessao){
        ModelAndView view = new ModelAndView("/vendas/carrinho");
        Carrinho carrinho = (Carrinho) sessao.getAttribute("carrinho");

        for(PedidoJogo pedido : carrinho.getJogosNoCarrinho()){
            if(pedido.getJogo().getId() == id){
                int qtde = pedido.getQuantidade();
                qtde += 1;
                pedido.setQuantidade(qtde);

                double subtotal = carrinho.calcularPrecoXQtde(pedido.getJogo(), qtde);
                pedido.setSubtotal(subtotal);
            }
        }
        
        double total = carrinho.calculaSubTotal(carrinho.getJogosNoCarrinho());
        carrinho.setTotal(total);
        sessao.setAttribute("carrinho", carrinho);
        view.addObject("jogos", carrinho.getJogosNoCarrinho());
        view.addObject("subtotal", carrinho.getTotal());

        return view;
    }
    
    @GetMapping("/diminuir/{id}")
    public ModelAndView diminuirQtdJogo(@PathVariable Long id, RequisicaoFormJogo requisicao, HttpSession sessao){
        ModelAndView view = new ModelAndView("/vendas/carrinho");
        Carrinho carrinho = (Carrinho) sessao.getAttribute("carrinho");
        for(PedidoJogo pedido : carrinho.getJogosNoCarrinho()){
            if(pedido.getJogo().getId() == id){
                int qtde = pedido.getQuantidade();
                qtde -= 1;
                if(qtde == 0){
                    // zerou a quantidade
                    carrinho.getJogosNoCarrinho().remove(pedido);
                    if(carrinho.getJogosNoCarrinho().isEmpty()){
                        // sem nenhum jogo no carrinho
                        return new ModelAndView("redirect:/home/fim");
                    }
                }else{
                    pedido.setQuantidade(qtde);

                    double subtotal = carrinho.calcularPrecoXQtde(pedido.getJogo(), qtde);
                    pedido.setSubtotal(subtotal);
                }
            }
        }
        double subTotal = carrinho.calculaSubTotal(carrinho.getJogosNoCarrinho());
        carrinho.setTotal(subTotal);
        sessao.setAttribute("carrinho", carrinho);
        view.addObject("jogos", carrinho.getJogosNoCarrinho());
        view.addObject("subtotal", carrinho.getTotal());

        return view;
    }

    @GetMapping("/remover/{id}")
    public ModelAndView removerItem (@PathVariable Long id, RequisicaoFormJogo requisicao, HttpSession sessao){
        ModelAndView view = new ModelAndView("/vendas/carrinho");
        Carrinho carrinho = (Carrinho) sessao.getAttribute("carrinho");
        for(PedidoJogo pedido : carrinho.getJogosNoCarrinho()){
            if(pedido.getJogo().getId() == id){
                carrinho.getJogosNoCarrinho().remove(pedido);
                
                if(carrinho.getJogosNoCarrinho().isEmpty()){
                    // sem nenhum jogo no carrinho
                    return new ModelAndView("redirect:/home/fim");
                }      
            }
        }
        double total = carrinho.calculaSubTotal(carrinho.getJogosNoCarrinho());
        carrinho.setTotal(total);
        sessao.setAttribute("carrinho", carrinho);
        view.addObject("jogos", carrinho.getJogosNoCarrinho());
        view.addObject("subtotal", carrinho.getTotal());

        return view;
    }


    @GetMapping("/carrinho")
    public ModelAndView exibirCarrinho (HttpSession sessao){
        if(sessao.isNew()){// não tem carrinho, pois sessão é nova
            return new ModelAndView("redirect:/home/fim");
        }
        ModelAndView view  = new ModelAndView("/vendas/carrinho");
        Carrinho carrinho = (Carrinho) sessao.getAttribute("carrinho");
        sessao.setAttribute("carrinho", carrinho);
        view.addObject("jogos", carrinho.getJogosNoCarrinho());
        view.addObject("subtotal", carrinho.getTotal());

        return view;
    }

    
    @PostMapping("/buscarCliente")
    public ModelAndView buscarCliente (RequisicaoFormCliente requisicaoCliente, RequisicaoFormEndereco requisicaoEndereco, HttpSession sessao){
        
        List<Cliente> resultado = this.clienteRepository.findByCpf(requisicaoCliente.getCpf());

        if(resultado.isEmpty()){
            // Não achou nenhum registro no banco de dados com esse CPF
            System.out.println("******* NÃO ACHOU CLIENTE COM ESSE CPF: " + requisicaoCliente.getCpf() + " *********");
            return new ModelAndView("redirect:/home");
            
        }else{
            System.out.println("Realizou a busca");
            Cliente cliente = resultado.get(0);

            ModelAndView view = new ModelAndView("/vendas/clienteEndereco");
            view.addObject("cliente", cliente);
            view.addObject("enderecos", cliente.getEnderecos());
            view.addObject("requisicaoCliente", requisicaoCliente);
            view.addObject("requisicaoEndereco", requisicaoEndereco);

            sessao.setAttribute("cliente", cliente);

            return view;
        }
    }

    
    @PostMapping ("/{id}/incluirNovoEndereco")
    public ModelAndView incluirEnderecoEntrega (@PathVariable Long id, RequisicaoFormEndereco requisicao, RequisicaoFormCartao cartaoForm, HttpSession sessao){
        // A próxima página a ser exibida
        ModelAndView view = new ModelAndView("/vendas/clientePagamento");

        // carrega o cliente cujo id vem pela requisição (apesar de estar na sessão)
        Cliente cliente = (Cliente) sessao.getAttribute("cliente");

        // Recebe o novo endereço de entrega
        Endereco endereco = new Endereco();
        endereco.setLogradouro(requisicao.getLogradouro());
        endereco.setNumero(requisicao.getNumero());
        endereco.setComplemento(requisicao.getComplemento());
        endereco.setBairro(requisicao.getBairro());
        endereco.setCidade(requisicao.getCidade());
        endereco.setUf(requisicao.getUf());
        endereco.setCep(requisicao.getCep());

        // vincula o novo endereçao ao cliente
        endereco.setCliente(cliente);

        // salva o novo endereço no banco de dados
        this.enderecoRepository.save(endereco);
        sessao.setAttribute("endereco", endereco);

        // Pega do banco os cartões do cliente para carregar na próxima página
        List<Cartao> cartoes = this.cartaoRepository.findByCliente(cliente);
        // Converte objetos Cartao -> PedidoCartao
        Set<PedidoCartao> cartoesNoPedido = new HashSet<>();
        for(int i = 0; i< cartoes.size(); i++){
            PedidoCartao cartaoCadastrado = new PedidoCartao();
            cartaoCadastrado.setCartao(cartoes.get(i));
            cartoesNoPedido.add(cartaoCadastrado);
        }
        
        
        sessao.setAttribute("cartoes", cartoesNoPedido);
        
        // recupera da sessão o carrinho de compras para ser exibido
        Carrinho carrinho = (Carrinho) sessao.getAttribute("carrinho");
        sessao.setAttribute("totalPedido", carrinho.getTotal());
        
        view.addObject("subtotal", carrinho.getTotal());
        view.addObject("jogos", carrinho.getJogosNoCarrinho());
        view.addObject("cliente", cliente);
        view.addObject("endereco", endereco);
        view.addObject("cartoes", cartoesNoPedido);
        view.addObject("cartaoForm", cartaoForm);

        return view;
    }

    

    @GetMapping ("/{id}/usarEsteEndereco")
    public ModelAndView usarEsteEnderecoEntrega (@PathVariable Long id, HttpSession sessao, RequisicaoFormCartao cartaoForm){
        ModelAndView view = new ModelAndView("/vendas/clientePagamento");

        // recupera do carrinho 
        Carrinho carrinho = (Carrinho) sessao.getAttribute("carrinho");
        Cliente cliente = (Cliente) sessao.getAttribute("cliente");
        
        // Pega do banco o endereço escolhido
        Optional<Endereco> resultado = this.enderecoRepository.findById(id);
        Endereco endereco = resultado.get();
        sessao.setAttribute("endereco", endereco);

        // Pega do banco os cartões do cliente para carregar na próxima página
        List<Cartao> cartoes = this.cartaoRepository.findByCliente(cliente);

        // Converte objetos Cartao -> PedidoCartao
        Set<PedidoCartao> cartoesNoPedido = new HashSet<>();
        for(int i = 0; i< cartoes.size(); i++){
            PedidoCartao cartaoCadastrado = new PedidoCartao();
            cartaoCadastrado.setCartao(cartoes.get(i));
            cartoesNoPedido.add(cartaoCadastrado);
        }

        sessao.setAttribute("cartoes", cartoesNoPedido);
        sessao.setAttribute("totalPedido", carrinho.getTotal());

        view.addObject("jogos", carrinho.getJogosNoCarrinho());
        view.addObject("cliente", cliente);
        view.addObject("endereco", endereco);
        view.addObject("cartoes", cartoesNoPedido);
        view.addObject("subtotal", carrinho.getTotal());
        view.addObject("cartaoForm", cartaoForm);
        
        return view;
    }

    
    @PostMapping("/finalizar")
    public ModelAndView finalizar (HttpSession sessao){
        sessao.invalidate();
        return new ModelAndView("redirect:/home");
    }
    
    @GetMapping("/fim")
    public ModelAndView fim (HttpSession sessao){
        sessao.invalidate();
        return new ModelAndView("redirect:/home");
    }
 
}
