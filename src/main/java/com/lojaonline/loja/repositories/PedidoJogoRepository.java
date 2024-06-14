package com.lojaonline.loja.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lojaonline.loja.models.Pedido;
import com.lojaonline.loja.models.PedidoJogo;
import java.util.List;


@Repository
public interface PedidoJogoRepository extends JpaRepository <PedidoJogo, Long>{
    List<PedidoJogo> findByPedido(Pedido pedido);
}
