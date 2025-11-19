package br.com.damasceno.postodecombustivel.service;

import br.com.damasceno.postodecombustivel.dto.ClienteCadastroDTO;
import br.com.damasceno.postodecombustivel.dto.ClienteResponseDTO;

public interface ClienteService {

    ClienteResponseDTO cadastrar(ClienteCadastroDTO dto);
}
