package br.com.damasceno.postodecombustivel.controller;

import br.com.damasceno.postodecombustivel.dto.ClienteCadastroDTO;
import br.com.damasceno.postodecombustivel.dto.ClienteResponseDTO;
import br.com.damasceno.postodecombustivel.service.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
@Tag(name = "Clientes (HubStation)",
    description = "Endpoints para cadastro de clientes e adesão ao fidelidade")
public class ClienteController {

  private final ClienteService service;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @Operation(summary = "Cadastra um novo cliente ou o adiciona ao programa de fidelidade")
  @ApiResponse(responseCode = "201",
      description = "Cliente cadastrado/atualizado e notificação enviada")
  public ClienteResponseDTO cadastrar(@Valid @RequestBody ClienteCadastroDTO dto) {
    return service.cadastrar(dto);
  }

  @GetMapping
  @Operation(summary = "Lista todos os clientes cadastrados")
  public List<ClienteResponseDTO> getAll() {
    return service.findAll();
  }

}
