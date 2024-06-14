package com.lojaonline.loja.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lojaonline.loja.models.Categoria;
import com.lojaonline.loja.models.Jogo;

import java.util.List;


@Repository
public interface JogoRepository extends JpaRepository<Jogo, Long >{
    List<Jogo> findByNome(String nome);
    List<Jogo> findByCategoria(Categoria categoria);
}
