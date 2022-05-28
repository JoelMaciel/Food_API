package com.joel.food.api.controller;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.joel.food.domain.exception.CozinhaNaoEncontradaException;
import com.joel.food.domain.exception.NegocioException;
import com.joel.food.domain.model.Restaurante;
import com.joel.food.domain.repository.RestauranteRepository;
import com.joel.food.domain.service.CadastroRestauranteService;

@RestController
@RequestMapping(value = "/restaurantes")
public class RestaurenteController {

	@Autowired
	private RestauranteRepository restauranteRepository;

	@Autowired
	private CadastroRestauranteService cadastroRestaurante;

	@GetMapping
	public List<Restaurante> listar() {
		return restauranteRepository.findAll();
	}

	@GetMapping("/{restauranteId}")
	public Restaurante buscar(@PathVariable Long restauranteId) {
		return cadastroRestaurante.buscarOuFalhar(restauranteId);

	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Restaurante adicionar(@RequestBody Restaurante restaurante) {
		try {
			return cadastroRestaurante.salvar(restaurante);
			
		} catch (CozinhaNaoEncontradaException e) {
			throw new NegocioException(e.getMessage());
		}

	}

	@PutMapping("/{restauranteId}")
	public Restaurante atualizar(@PathVariable Long restauranteId, @RequestBody Restaurante restaruante) {

		Restaurante restauranteAtual = cadastroRestaurante.buscarOuFalhar(restauranteId);

		BeanUtils.copyProperties(restaruante, restauranteAtual, "id", "formasPagamento", "endereco", "dataCadastro",
				"produtos");
		try {
			return cadastroRestaurante.salvar(restauranteAtual);
			
		} catch (CozinhaNaoEncontradaException e) {
			throw new NegocioException(e.getLocalizedMessage());
		}

	}

	@PatchMapping("/{restauranteId}")
	public Restaurante atualizarParcial(@PathVariable Long restauranteId, @RequestBody Map<String, Object> campos) {
		Restaurante restauranteAtual = cadastroRestaurante.buscarOuFalhar(restauranteId);

		merge(campos, restauranteAtual);
		return atualizar(restauranteId, restauranteAtual);

	}

	private void merge(Map<String, Object> dadosOrigem, Restaurante restauranteDestino) {
		ObjectMapper objectMapper = new ObjectMapper();
		Restaurante restauranteOrigem = objectMapper.convertValue(dadosOrigem, Restaurante.class);

		dadosOrigem.forEach((nomePropriedade, valorPropriedade) -> {
			Field field = ReflectionUtils.findField(Restaurante.class, nomePropriedade);
			field.setAccessible(true);

			Object novoValor = ReflectionUtils.getField(field, restauranteOrigem);

			System.out.println(nomePropriedade + " = " + valorPropriedade + " = " + novoValor);
			ReflectionUtils.setField(field, restauranteDestino, novoValor);
		});
	}
}
