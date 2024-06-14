package com.lojaonline.loja.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lojaonline.loja.models.Pedido;
import com.lojaonline.loja.models.PedidoCartao;
import java.util.List;


@Repository
public interface PedidoCartaoRepository extends JpaRepository <PedidoCartao, Long>{
    List<PedidoCartao> findByPedido(Pedido pedido);
}
