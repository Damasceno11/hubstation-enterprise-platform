package br.com.damasceno.postodecombustivel.service;

import br.com.damasceno.postodecombustivel.dto.TipoCombustivelRequestDTO;
import br.com.damasceno.postodecombustivel.dto.TipoCombustivelResponseDTO;

import java.util.List;

public interface TipoCombustivelService {

    TipoCombustivelResponseDTO create(TipoCombustivelRequestDTO requestDTO);

    List<TipoCombustivelResponseDTO> findAll();

    TipoCombustivelResponseDTO findById(Long id);

    TipoCombustivelResponseDTO update(Long id, TipoCombustivelRequestDTO requestDTO);

    void deleteById(Long id);
}
