package br.com.damasceno.postodecombustivel.service;

import java.util.List;
import br.com.damasceno.postodecombustivel.dto.ClienteCadastroDTO;
import br.com.damasceno.postodecombustivel.dto.ClienteResponseDTO;

public interface ClienteService {

  ClienteResponseDTO cadastrar(ClienteCadastroDTO dto);

  List<ClienteResponseDTO> findAll();
}
