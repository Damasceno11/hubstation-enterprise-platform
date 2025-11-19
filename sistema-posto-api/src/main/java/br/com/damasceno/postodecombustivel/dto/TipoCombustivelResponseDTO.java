package br.com.damasceno.postodecombustivel.dto;

import java.math.BigDecimal;

public record TipoCombustivelResponseDTO(
        Long id,
        String nome,
        BigDecimal precoLitro
) {}
