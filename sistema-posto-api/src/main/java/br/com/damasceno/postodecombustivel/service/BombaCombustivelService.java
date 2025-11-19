package br.com.damasceno.postodecombustivel.service;

import br.com.damasceno.postodecombustivel.dto.BombaCombustivelRequestDTO;
import br.com.damasceno.postodecombustivel.dto.BombaCombustivelResponseDTO;

import java.util.List;

public interface BombaCombustivelService {

    BombaCombustivelResponseDTO create(BombaCombustivelRequestDTO requestDTO);

    List<BombaCombustivelResponseDTO> findAll();

    BombaCombustivelResponseDTO findById(Long id);

    BombaCombustivelResponseDTO update(Long id, BombaCombustivelRequestDTO requestDTO);

    void delete(Long id);
}
