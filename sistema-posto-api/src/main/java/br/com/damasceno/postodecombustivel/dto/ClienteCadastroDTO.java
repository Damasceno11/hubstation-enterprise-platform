package br.com.damasceno.postodecombustivel.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.br.CPF;


public record ClienteCadastroDTO(

        @NotBlank(message = "Nome não pode ser vazio")
        String nome,

        @NotBlank(message = "CPF não pode ser vazio")
        @CPF(message = "CPF inválido")
        String cpf,

        @NotBlank(message = "E-mail não pode ser vazio")
        @Email(message = "Formato de e-mail inválido")
        String email
) {}
