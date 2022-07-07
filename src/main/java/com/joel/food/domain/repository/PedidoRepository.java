package com.joel.food.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.joel.food.domain.model.Pedido;

@Repository
public interface  PedidoRepository extends CustomJpaRepository<Pedido, Long>{
	
	@Query("from Pedido p join fetch p.cliente join fetch p.restaurante r join fetch r.cozinha")
	List<Pedido> findAll();

}