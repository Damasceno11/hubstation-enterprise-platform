package br.com.damasceno.postodecombustivel.service.impl;

import br.com.damasceno.postodecombustivel.dto.TipoCombustivelRequestDTO;
import br.com.damasceno.postodecombustivel.dto.TipoCombustivelResponseDTO;
import br.com.damasceno.postodecombustivel.exception.BusinessException;
import br.com.damasceno.postodecombustivel.exception.ResourceNotFoundException;
import br.com.damasceno.postodecombustivel.mapper.TipoCombustivelMapper;
import br.com.damasceno.postodecombustivel.model.TipoCombustivel;
import br.com.damasceno.postodecombustivel.repository.TipoCombustivelRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TipoCombustivelServiceImplTest {

    @Mock
    private TipoCombustivelRepository repository;

    @Mock private TipoCombustivelMapper mapper;

    @InjectMocks
    private TipoCombustivelServiceImpl service;

    private TipoCombustivel combustivel;
    private TipoCombustivelRequestDTO requestDTO;
    private TipoCombustivelResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        combustivel = new TipoCombustivel(1L, "Gasolina Comum", new BigDecimal("5.50"));
        requestDTO = new TipoCombustivelRequestDTO("Gasolina Comum", new BigDecimal("5.50"));
        responseDTO = new TipoCombustivelResponseDTO(1L, "Gasolina Comum", new BigDecimal("5.50"));
    }

    // --- Teste Caminho Feliz ---

    @Test
    @DisplayName("Deve criar um TipoCombustivel com sucesso")
    void createShouldReturnTipoCombustivelDTO() {
        // Arrange
        when(repository.findByNome(requestDTO.nome())).thenReturn(Optional.empty());
        when(mapper.toEntity(any(TipoCombustivelRequestDTO.class))).thenReturn(combustivel);
        when(repository.save(any(TipoCombustivel.class))).thenReturn(combustivel);
        when(mapper.toResponseDTO(any(TipoCombustivel.class))).thenReturn(responseDTO);

        // Act
        TipoCombustivelResponseDTO result = service.create(requestDTO);

        // Assert
        assertNotNull(result);
        assertEquals(responseDTO.id(), result.id());
        assertEquals(responseDTO.nome(), result.nome());
        verify(repository, times(1)).save(combustivel);
    }

    // --- Testes de Exceção e Regra de Negócio ---

    @Test
    @DisplayName("Deve lançar BusinessException ao tentar criar com nome duplicado")
    void createShouldThrowBusinessExceptionWhenNomeExists() {
        // Arrange
        when(repository.findByNome(requestDTO.nome())).thenReturn(Optional.of(combustivel));

        // Act & Assert
        assertThrows(
                BusinessException.class,
                () -> service.create(requestDTO),
                "Deveria lançar BusinessException por nome duplicado");
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException ao buscar ID inexistente")
    void findByIdShouldThrowResourceNotFound() {
        // Arrange
        when(repository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(
                ResourceNotFoundException.class,
                () -> service.findById(99L),
                "Deveria lançar ResourceNotFoundException");
    }

    @Test
    @DisplayName("Deve lançar BusinessException ao atualizar com nome duplicado em outra entidade")
    void updateShouldThrowBusinessExceptionWhenNomeExistsInAnotherEntity() {
        // Arrange
        TipoCombustivelRequestDTO updateRequest =
                new TipoCombustivelRequestDTO("Gasolina Aditivada", new BigDecimal("5.80"));
        TipoCombustivel entidadeConcorrente =
                new TipoCombustivel(2L, "Gasolina Aditivada", new BigDecimal("5.70"));

        when(repository.findById(1L)).thenReturn(Optional.of(combustivel));
        when(repository.findByNome(updateRequest.nome())).thenReturn(Optional.of(entidadeConcorrente));

        // Act & Assert
        assertThrows(
                BusinessException.class,
                () -> service.update(1L, updateRequest),
                "Deveria lançar BusinessException pois o nome já existe no ID 2");
        verify(repository, never()).save(any());
    }

    // --- Testes de Listagem ---

    @Test
    @DisplayName("Deve retornar lista vazia quando findAll não encontrar registros")
    void findAllShouldReturnEmptyList() {
        // Arrange
        when(repository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<TipoCombustivelResponseDTO> result = service.findAll();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
