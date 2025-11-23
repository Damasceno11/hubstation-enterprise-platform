package br.com.damasceno.postodecombustivel.controller;

import br.com.damasceno.postodecombustivel.dto.TipoCombustivelRequestDTO;
import br.com.damasceno.postodecombustivel.dto.TipoCombustivelResponseDTO;
import br.com.damasceno.postodecombustivel.exception.ErrorResponseDTO;
import br.com.damasceno.postodecombustivel.service.TipoCombustivelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tipos-combustivel")
@RequiredArgsConstructor
@Tag(name = "Tipos de Combustível",
    description = "Endpoints para gerenciar os tipos de combustível")
public class TipoCombustivelController {

  private final TipoCombustivelService service;

  @Operation(summary = "Cria um novo tipo de combustível")
  @ApiResponses(
      value = {@ApiResponse(responseCode = "201", description = "Combustível criado com sucesso"),
          @ApiResponse(responseCode = "400",
              description = "Dados de entrada inválidos ou regra de negócio violada",
              content = @Content(mediaType = "application/json",
                  schema = @Schema(
                      oneOf = {ErrorResponseDTO.class, TipoCombustivelRequestDTO.class})))})
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public TipoCombustivelResponseDTO create(
      @Valid @RequestBody TipoCombustivelRequestDTO requestDTO) {
    return service.create(requestDTO);
  }

  @Operation(summary = "Lista todos os tipos de combustível")
  @GetMapping
  public List<TipoCombustivelResponseDTO> getAll() {
    return service.findAll();
  }

  @Operation(summary = "Busca um tipo de combustível por ID")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Combustível encontrado"),
      @ApiResponse(responseCode = "404", description = "Combustível não encontrado",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponseDTO.class)))})
  @GetMapping("/{id}")
  public TipoCombustivelResponseDTO getById(@PathVariable Long id) {
    return service.findById(id);
  }

  @Operation(summary = "Atualiza um tipo de combustível existente")
  @PutMapping("/{id}")
  public TipoCombustivelResponseDTO update(@PathVariable Long id,
      @Valid @RequestBody TipoCombustivelRequestDTO requestDTO) {
    return service.update(id, requestDTO);
  }

  @Operation(summary = "Deleta um tipo de combustível por ID")
  @ApiResponses(
      value = {@ApiResponse(responseCode = "204", description = "Combustível deleatdo com sucesso"),
          @ApiResponse(responseCode = "404", description = "Combustível não encontrado")})
  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable Long id) {
    service.deleteById(id);
  }

}
