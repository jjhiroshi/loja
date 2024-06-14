package com.lojaonline.loja.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lojaonline.loja.models.Cartao;
import com.lojaonline.loja.models.Cliente;

import java.util.List;


@Repository
public interface CartaoRepository extends JpaRepository<Cartao, Long>{
    List<Cartao> findByCliente(Cliente cliente);
}
