package br.com.damasceno.postodecombustivel.dto;


public record BombaCombustivelResponseDTO(Long id, String nome,
    TipoCombustivelResponseDTO tipoCombustivel) {
}
