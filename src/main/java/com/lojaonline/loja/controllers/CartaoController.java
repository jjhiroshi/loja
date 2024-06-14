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
import com.lojaonline.loja.models.Carrinho;
import com.lojaonline.loja.models.Cartao;
import com.lojaonline.loja.models.Cliente;
import com.lojaonline.loja.models.Endereco;
import com.lojaonline.loja.models.PedidoCartao;
import com.lojaonline.loja.repositories.CartaoRepository;
import com.lojaonline.loja.repositories.ClienteRepository;

import jakarta.servlet.http.HttpSession;


@Controller
@RequestMapping("/cartoes")
public class CartaoController {

    @Autowired
    private CartaoRepository cartaoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    
    @GetMapping("/{id}/excluir")
    public ModelAndView excluirCartao (@PathVariable Long id, RequisicaoFormCartao cartaoDTO){

        // Próxima página a carregar
        ModelAndView view = new ModelAndView("/clientes/exibirCliente");

        // procura o cartao com esse ID
        Optional<Cartao> opcional = this.cartaoRepository.findById(id);
        
        // Converte de Optional para Cartao
        Cartao cartao = opcional.get();

        // recuperar o cliente deste cartao
        Cliente cliente = cartao.getCliente();
        Optional<Cliente> clienteOpcional = this.clienteRepository.findById(cliente.getId());
        cliente = clienteOpcional.get();
        
        // adciona os dados do cliente na view
        view.addObject("cliente", cliente);
        view.addObject("enderecos", cliente.getEnderecos());

        // deleta o cartão solicitado via ID
        this.cartaoRepository.deleteById(id);

        // Faz a busca dos cartões restantes do cliente
        List<Cartao> cartoes = this.cartaoRepository.findByCliente(cliente);
        // e adiciona na view
        view.addObject("cartoes", cartoes);
        
        return view;
    }
    
    @PostMapping("/incluir")
    public ModelAndView incluirCartao (HttpSession sessao,  RequisicaoFormCartao cartaoForm){
        ModelAndView view = new ModelAndView("/vendas/clientePagamento");

        // Recuperar o cliente da sessão
        Cliente cliente = (Cliente) sessao.getAttribute("cliente");
        
        // salva o cliente a quem pertence o cartão
        cartaoForm.setCliente(cliente);
        // salva os dados do cartao para ser incluído
        Cartao cartao = cartaoForm.toCartao();

        this.cartaoRepository.save(cartao);

        Carrinho carrinho = (Carrinho) sessao.getAttribute("carrinho");
        Endereco endereco = (Endereco) sessao.getAttribute("endereco");

        List<Cartao> cartoes = this.cartaoRepository.findByCliente(cliente);
         // Converte objetos Cartao -> PedidoCartao
        Set<PedidoCartao> cartoesNoPedido = new HashSet<>();
        for(int i = 0; i< cartoes.size(); i++){
            PedidoCartao cartaoCadastrado = new PedidoCartao();
            cartaoCadastrado.setCartao(cartoes.get(i));
            cartoesNoPedido.add(cartaoCadastrado);
        }
        
        sessao.setAttribute("cartoes", cartoesNoPedido);

        view.addObject("jogos", carrinho.getJogosNoCarrinho());
        view.addObject("cliente", cliente);
        view.addObject("endereco", endereco);
        view.addObject("subtotal", carrinho.getTotal());
        view.addObject("cartoes", cartoesNoPedido);
        view.addObject("cartaoForm", cartaoForm);

        return view;
    }
    
    @PostMapping("/{id}/atualizarValor")
    public ModelAndView atualizarValor (@PathVariable Long id, HttpSession sessao,  RequisicaoFormCartao cartaoForm){
        ModelAndView view = new ModelAndView("/vendas/clientePagamento");

        Double totalCobradoNosCartoes = 0.0;

        // localizar o cartão e inserir o valor que o cliente que gastar naquele cartão
        Set<PedidoCartao> cartoes = (Set<PedidoCartao>) sessao.getAttribute("cartoes");
        for(PedidoCartao cartao : cartoes){
            if(id.equals(cartao.getCartao().getId())){ // mesmo cartão?
                // sim, atualizar o valor a ser cobrado nesse cartao
                cartao.setValor(cartaoForm.getValor());
            }
        }
        // recalculando e atualizando o total cobrado nos cartoes
        for(PedidoCartao cartao : cartoes){
            if(cartao.getValor() != null){
                totalCobradoNosCartoes += cartao.getValor();
            }
        }
        
        sessao.setAttribute("cartoes", cartoes);
        sessao.setAttribute("totalCobrado", totalCobradoNosCartoes);
        

        // Recuperar o cliente, endereço e carrinho da sessão
        Cliente cliente = (Cliente) sessao.getAttribute("cliente");
        Carrinho carrinho = (Carrinho) sessao.getAttribute("carrinho");
        Endereco endereco = (Endereco) sessao.getAttribute("endereco");

        sessao.setAttribute("totalPedido", carrinho.getTotal());

        view.addObject("jogos", carrinho.getJogosNoCarrinho());
        view.addObject("cliente", cliente);
        view.addObject("endereco", endereco);
        view.addObject("subtotal", carrinho.getTotal());
        view.addObject("cartoes", cartoes);
        view.addObject("cartaoForm", cartaoForm);
        view.addObject("totalCobrado", totalCobradoNosCartoes);

        if(carrinho.getTotal() > totalCobradoNosCartoes){
            String aviso = "Saldo Insuficiente";
            view.addObject("aviso", aviso);
        }else if(carrinho.getTotal() < totalCobradoNosCartoes){
            String aviso = "Valor cobrado excedido";
            view.addObject("aviso", aviso);
        }else if(totalCobradoNosCartoes.equals(carrinho.getTotal())){
            String aviso = "";
            view.addObject("aviso", aviso);
        }
        return view;
    }
    
}
