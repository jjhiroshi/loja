package com.lojaonline.loja.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lojaonline.loja.models.Cliente;

import java.util.List;


@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long>{
    List<Cliente> findByCpf(String cpf);
}
