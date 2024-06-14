package com.lojaonline.loja.controllers;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;


import com.lojaonline.loja.dto.RequisicaoFormCartao;
import com.lojaonline.loja.models.Carrinho;
import com.lojaonline.loja.models.Cliente;
import com.lojaonline.loja.models.Endereco;
import com.lojaonline.loja.models.Pedido;
import com.lojaonline.loja.models.PedidoCartao;
import com.lojaonline.loja.models.PedidoJogo;
import com.lojaonline.loja.models.Situacao;
import com.lojaonline.loja.repositories.PedidoCartaoRepository;
import com.lojaonline.loja.repositories.PedidoJogoRepository;
import com.lojaonline.loja.repositories.PedidoRepository;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/pedidos")
public class PedidoController {

    @Autowired
    PedidoRepository pedidoRepository;

    @Autowired
    PedidoJogoRepository pedidoJogoRepository;

    @Autowired
    PedidoCartaoRepository pedidoCartaoRepository;
    
    
    @GetMapping("/novo")
    public ModelAndView novoPedido(HttpSession sessao, RequisicaoFormCartao cartaoForm){
    
        // Recuperar o cliente, endereço e carrinho da sessão
        Cliente cliente = (Cliente) sessao.getAttribute("cliente");
        Carrinho carrinho = (Carrinho) sessao.getAttribute("carrinho");
        Endereco endereco = (Endereco) sessao.getAttribute("endereco");
        Set<PedidoCartao> cartoes = (Set<PedidoCartao>) sessao.getAttribute("cartoes");

        Double valorCobrado = (Double) sessao.getAttribute("totalCobrado");
        Double valorPedido = carrinho.getTotal();

        if(valorPedido > valorCobrado){
            // se o cliente finalizou o pedido com valor insufiente
            String aviso = "Saldo Insuficiente";
            ModelAndView view = new ModelAndView("/vendas/clientePagamento");
            view.addObject("aviso", aviso);

            view.addObject("jogos", carrinho.getJogosNoCarrinho());
            view.addObject("cliente", cliente);
            view.addObject("endereco", endereco);
            view.addObject("subtotal", valorPedido);
            view.addObject("cartoes", cartoes);
            view.addObject("cartaoForm", cartaoForm);
            view.addObject("totalCobrado", valorCobrado);
            
            return view;
        }else if(valorPedido < valorCobrado){
            // se o cliente finalizou o valor cobrado nos cartões maior que o do pedido
            String aviso = "Valor cobrado excedido";
            ModelAndView view = new ModelAndView("/vendas/clientePagamento");
            view.addObject("aviso", aviso);

            view.addObject("jogos", carrinho.getJogosNoCarrinho());
            view.addObject("cliente", cliente);
            view.addObject("endereco", endereco);
            view.addObject("subtotal", valorPedido);
            view.addObject("cartoes", cartoes);
            view.addObject("cartaoForm", cartaoForm);
            view.addObject("totalCobrado", valorCobrado);
            return view;
        }
            
        ModelAndView view = new ModelAndView("/vendas/novoPedido");

        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setEnderecoDeEntrega(endereco);

        Situacao situacao = Situacao.PEDIDO_EM_PROCESSAMENTO;        
        pedido.setSituacao(situacao);

        Date dataAtual = new Date();
        SimpleDateFormat dataParse = new SimpleDateFormat("dd/MM/yyyy  HH:MM");
        pedido.setDataDoPedido(dataAtual);

        pedido.setTotal(valorPedido);
        
        this.pedidoRepository.save(pedido);

        // Os jogos e os Cartões são salvos em outras tabelas
        Set<PedidoJogo> jogos = carrinho.getJogosNoCarrinho();

        for (PedidoJogo jogo : jogos){
            jogo.setPedido(pedido);
            this.pedidoJogoRepository.save(jogo);
        }
        for(PedidoCartao cartao : cartoes){
            cartao.setPedido(pedido);
            this.pedidoCartaoRepository.save(cartao);
        }

        sessao.invalidate();

        view.addObject("numeroPedido", pedido.getId());
        view.addObject("statusPedido", pedido.getSituacao());
        view.addObject("dataPedido", dataParse.format(dataAtual));
        view.addObject("cliente", cliente);
        view.addObject("endereco", endereco);
        view.addObject("jogos", carrinho.getJogosNoCarrinho());
        view.addObject("cartoes", cartoes);
        view.addObject("valorPedido", valorPedido);

        return view;
        
    }
    
    
    @GetMapping("")
    public ModelAndView adminlistarPedido(){
        ModelAndView view = new ModelAndView("/vendas/admListarPedidos");

        // pesquisa todos os pedidos da loja
        List<Pedido> pedidos = this.pedidoRepository.findAll();
        view.addObject("pedidos", pedidos);
        return view;
    }

    
    @GetMapping("/{id}/detalhes")
    public ModelAndView adminDetalharPedido(@PathVariable Long id){
        // Carrega a página HTML que abrirá
        ModelAndView view = new ModelAndView("/vendas/detalhesPedido");

        // Busca o pedido via ID
        Optional<Pedido> opcional = this.pedidoRepository.findById(id);
        // Converte para uma classe Pedido
        Pedido pedido = opcional.get();

        List<PedidoCartao> cartoesUtilizados = this.pedidoCartaoRepository.findByPedido(pedido);
        List<PedidoJogo> jogosComprados = this.pedidoJogoRepository.findByPedido(pedido);

        SimpleDateFormat dataParse = new SimpleDateFormat("dd/MM/yyyy  HH:MM");

        view.addObject("numeroPedido", pedido.getId());
        view.addObject("statusPedido", pedido.getSituacao());
        view.addObject("dataPedido", dataParse.format(pedido.getDataDoPedido()));
        view.addObject("cliente", pedido.getCliente());
        view.addObject("endereco", pedido.getEnderecoDeEntrega());
        view.addObject("jogos", jogosComprados);
        view.addObject("cartoes", cartoesUtilizados);
        view.addObject("valorPedido", pedido.getTotal());

        return view;
    }
    

    /*
    IMPLEMENTAR O MÉTODO PARA O CLIENTE CONSULTAR SEUS PEDIDOS
     */
    
    @GetMapping("/{id}/excluir")
    public ModelAndView excluir(@PathVariable Long id){
        ModelAndView view = new ModelAndView("redirect:/pedidos");
        try{
            this.pedidoRepository.deleteById(id);
            return view;
        }catch (EmptyResultDataAccessException e){
            System.out.println("Exceção Ocorrida: " + e);
            return view;
        }
        
    }
}
