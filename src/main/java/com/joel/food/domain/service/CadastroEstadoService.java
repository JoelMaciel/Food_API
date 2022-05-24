package com.joel.food.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.joel.food.domain.exception.EntidadeEmUsoException;
import com.joel.food.domain.exception.EntidadeNaoEncontradaException;
import com.joel.food.domain.model.Estado;
import com.joel.food.domain.repository.EstadoRepository;

@Service
public class CadastroEstadoService {
	
	@Autowired
	private EstadoRepository estadoRepository;
	
	public Estado salvar(Estado estado) {
		return estadoRepository.save(estado);
	}
	
	
	public void excluir(Long estadoId) {
		try {
			estadoRepository.deleteById(estadoId);
			
			
		} catch (EmptyResultDataAccessException e) {
			throw new EntidadeNaoEncontradaException(String.format("Não existe um cadastro de estado com o código %d", estadoId));
		}catch(DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(String.format("Estado de código %d não pode ser removido pois está em uso", estadoId));
		}
		
	}

}















