package br.com.damasceno.postodecombustivel.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record BombaCombustivelRequestDTO(
        @NotBlank(message = "O nome não pode estar em branco")
        @Size(max = 100, message = "O nome não pode esceder 100 caracteres")
        String nome,

        @NotNull(message = "O ID do tipo do combustivel não pode ser nulo")
        Long tipoCombustivelId
) {}
