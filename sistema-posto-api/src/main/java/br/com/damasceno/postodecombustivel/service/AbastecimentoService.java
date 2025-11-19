package br.com.damasceno.postodecombustivel.service;

import br.com.damasceno.postodecombustivel.dto.AbastecimentoRequestDTO;
import br.com.damasceno.postodecombustivel.dto.AbastecimentoResponseDTO;

import java.util.List;

public interface AbastecimentoService {

    AbastecimentoResponseDTO create(AbastecimentoRequestDTO requestDTO);

    List<AbastecimentoResponseDTO> findAll();

    AbastecimentoResponseDTO findById(Long id);

    AbastecimentoResponseDTO cancelar(Long id);
}
