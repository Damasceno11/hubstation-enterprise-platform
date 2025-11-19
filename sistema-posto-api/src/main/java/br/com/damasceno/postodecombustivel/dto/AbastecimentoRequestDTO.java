package br.com.damasceno.postodecombustivel.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.br.CPF;

import java.math.BigDecimal;
import java.time.LocalDate;

public record AbastecimentoRequestDTO(
        @NotNull(message = "O iD da bomba de combustível não pode ser nulo")
        Long bombaId,

        @NotNull(message = "A data não pode ser nula")
        LocalDate dataAbastecimento,

        @DecimalMin(value = "0.001", message = "Litragem deve ser positiva")
        BigDecimal litragem,

        @CPF(message = "CPF inválido") // Opcional mas se vier, tem que ser válido
        String cpfCliente
) {}
