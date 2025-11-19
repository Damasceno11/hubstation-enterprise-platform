package br.com.damasceno.postodecombustivel.dto;

import br.com.damasceno.postodecombustivel.model.TipoCombustivel;

public record BombaCombustivelResponseDTO(
        Long id,
        String nome,
        TipoCombustivelResponseDTO tipoCombustivel
) {}
