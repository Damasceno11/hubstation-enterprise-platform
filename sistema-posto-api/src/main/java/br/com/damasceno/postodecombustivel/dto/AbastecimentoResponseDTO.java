package br.com.damasceno.postodecombustivel.dto;

import br.com.damasceno.postodecombustivel.model.StatusAbastecimento;

import java.math.BigDecimal;
import java.time.LocalDate;

public record AbastecimentoResponseDTO(
        Long id,
        LocalDate dataAbastecimento,
        BigDecimal litragem,
        BigDecimal valorTotal,
        BombaCombustivelResponseDTO bombaCombustivel,
        StatusAbastecimento status,
        ClienteResponseDTO cliente // Pode ser nulo se abastecimento for anônimo
) {}
