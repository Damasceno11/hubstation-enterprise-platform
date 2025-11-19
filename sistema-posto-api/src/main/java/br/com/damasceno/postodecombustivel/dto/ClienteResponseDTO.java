package br.com.damasceno.postodecombustivel.dto;

public record ClienteResponseDTO(
        Long id,
        String nome,
        String cpf,
        String email
) {}
