package br.com.damasceno.postodecombustivel.controller;

import br.com.damasceno.postodecombustivel.dto.AbastecimentoRequestDTO;
import br.com.damasceno.postodecombustivel.dto.AbastecimentoResponseDTO;
import br.com.damasceno.postodecombustivel.exception.ErrorResponseDTO;
import br.com.damasceno.postodecombustivel.service.AbastecimentoService;
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
@RequestMapping("/api/abastecimentos")
@RequiredArgsConstructor
@Tag(name = "Abastecimentos", description = "Endpoints para registrar e consultar abastecimentos")
public class AbastecimentoController {

    private final AbastecimentoService abastecimentoService;

    @Operation(summary = "Resgistra um novo abastecimento (calculado)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Abastecimento registrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos (ex: litragem e valor preenchidos)",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Bomba de combustível não encontrada")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AbastecimentoResponseDTO create(@Valid @RequestBody AbastecimentoRequestDTO requestDTO) {
        return abastecimentoService.create(requestDTO);
    }

    @Operation(summary = "Lista todos os abastecimentos registrados")
    @GetMapping
    public List<AbastecimentoResponseDTO> getAll() {
        return abastecimentoService.findAll();
    }

    @Operation(summary = "Busca um abastecimento por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Abastecimento encontrado"),
            @ApiResponse(responseCode = "404", description = "Abastecimento não encontrado")
    })
    @GetMapping("/{id}")
    public AbastecimentoResponseDTO getById(@PathVariable Long id) {
        return abastecimentoService.findById(id);
    }

    @Operation(summary = "Cancela um abastecimento (muda o status para CANCELADO)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Abastecimento CANCELADO com sucesso"),
            @ApiResponse(responseCode = "400", description = "Abastecimento já estava cancelado"),
            @ApiResponse(responseCode = "404", description = "Abastecimento não encontrado")
    })
    @PatchMapping("/{id}/cancelar")
    public AbastecimentoResponseDTO cancelar(@PathVariable Long id) {
        return abastecimentoService.cancelar(id);
    }

}
