package br.com.damasceno.postodecombustivel.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record TipoCombustivelRequestDTO(
        @NotBlank(message = "O nome não pode estar em branco")
        @Size(max = 100, message = "O nome não pode exceder 100 caracteres")
        String nome,

        @NotNull(message = "O preço por litro não pode ser nulo")
        @DecimalMin(value = "0.01", message = "O preço deve ser positivo")
        BigDecimal precoLitro
) {}
