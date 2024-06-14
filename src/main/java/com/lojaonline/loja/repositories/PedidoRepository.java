package com.lojaonline.loja.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lojaonline.loja.models.Pedido;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long>{

}