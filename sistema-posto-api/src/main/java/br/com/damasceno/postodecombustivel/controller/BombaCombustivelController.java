package br.com.damasceno.postodecombustivel.controller;

import br.com.damasceno.postodecombustivel.dto.BombaCombustivelRequestDTO;
import br.com.damasceno.postodecombustivel.dto.BombaCombustivelResponseDTO;
import br.com.damasceno.postodecombustivel.model.BombaCombustivel;
import br.com.damasceno.postodecombustivel.service.BombaCombustivelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bombas")
@RequiredArgsConstructor
@Tag(name = "Bombas de Combustível", description = "Endpoints para gerenciar as bombas")
public class BombaCombustivelController {

    private final BombaCombustivelService service;

    @Operation(summary = "Cria uma nova bomba de combustível")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Bomba criada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Tipo de Combustível (pai) não encontrado")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BombaCombustivelResponseDTO create(@Valid @RequestBody BombaCombustivelRequestDTO requestDTO) {
        return service.create(requestDTO);
    }

    @Operation(summary = "Lista todas as bombas de combustível")
    @GetMapping
    public List<BombaCombustivelResponseDTO> getAll() {
        return service.findAll();
    }

    @Operation(summary = "Busca uma bomba de combustível por ID")
    @GetMapping("/{id}")
    public  BombaCombustivelResponseDTO getById(@PathVariable Long id) {
        return  service.findById(id);
    }

    @Operation(summary = "Atualizar uma bomba de combustível existente")
    @PutMapping("/{id}")
    public BombaCombustivelResponseDTO update(@PathVariable Long id, @Valid @RequestBody BombaCombustivelRequestDTO requestDTO) {
        return service.update(id, requestDTO);
    }

    @Operation(summary = "Deletar uma bomba de combustível por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Bomba de Combustível deletada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Bomba de Combustível não encontrada")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

}
