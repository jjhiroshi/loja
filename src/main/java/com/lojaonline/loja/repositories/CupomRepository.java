package com.lojaonline.loja.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lojaonline.loja.models.Cupom;

@Repository
public interface CupomRepository extends JpaRepository<Cupom, Long>{

}
