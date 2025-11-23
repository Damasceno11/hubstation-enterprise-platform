package br.com.damasceno.postodecombustivel.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import br.com.damasceno.postodecombustivel.dto.BombaCombustivelRequestDTO;
import br.com.damasceno.postodecombustivel.dto.BombaCombustivelResponseDTO;
import br.com.damasceno.postodecombustivel.dto.TipoCombustivelResponseDTO;
import br.com.damasceno.postodecombustivel.exception.ResourceNotFoundException;
import br.com.damasceno.postodecombustivel.mapper.BombaCombustivelMapper;
import br.com.damasceno.postodecombustivel.mapper.TipoCombustivelMapper;
import br.com.damasceno.postodecombustivel.model.BombaCombustivel;
import br.com.damasceno.postodecombustivel.model.TipoCombustivel;
import br.com.damasceno.postodecombustivel.repository.BombaCombustivelRepository;
import br.com.damasceno.postodecombustivel.repository.TipoCombustivelRepository;
import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BombaCombustivelServiceImplTest {

  @Mock
  private BombaCombustivelRepository bombaRepository;

  @Mock
  private TipoCombustivelRepository tipoCombustivelRepository;

  @Mock
  private BombaCombustivelMapper mapper;

  // Usamos @Spy no mapper real para testar o mapeamento aninhado
  @Spy
  private TipoCombustivelMapper tipoCombustivelMapper = new TipoCombustivelMapper();

  @InjectMocks
  private BombaCombustivelServiceImpl service;

  private TipoCombustivel tipoCombustivel;
  private BombaCombustivel bombaCombustivel;
  private BombaCombustivelRequestDTO requestDTO;
  private BombaCombustivelResponseDTO responseDTO;

  @BeforeEach
  void setUp() {
    tipoCombustivel = new TipoCombustivel(1L, "Gasolina Comum", new BigDecimal("5.50"));
    bombaCombustivel = new BombaCombustivel(1L, "Bomba 01", tipoCombustivel);
    requestDTO = new BombaCombustivelRequestDTO("Bomba 01", 1L);

    responseDTO = new BombaCombustivelResponseDTO(1L, "Bomba 01",
        new TipoCombustivelResponseDTO(1L, "Gasolina Comum", new BigDecimal("5.50")));
  }

  @Test
  @DisplayName("Deve criar uma BombaCombustivel com sucesso")
  void createShouldReturnBombaCombustivelDTO() {
    // Arrange
    // 1. Simula a busca do TipoCombustivel (deve existir)
    when(tipoCombustivelRepository.findById(1L)).thenReturn(Optional.of(tipoCombustivel));

    // 2. Simula o mapeamento do DTO para a entidade (parcial, sem o tipo)
    BombaCombustivel bombaParcial = new BombaCombustivel();
    bombaParcial.setNome("Bomba 01");
    when(mapper.toEntity(requestDTO)).thenReturn(bombaParcial);

    // 3. Simula o salvamento da entidade (agora completa)
    when(bombaRepository.save(any(BombaCombustivel.class))).thenReturn(bombaCombustivel);

    // 4. Simula o mapeamento para o DTO de resposta
    when(mapper.toResponseDto(bombaCombustivel)).thenReturn(responseDTO);

    // Act
    BombaCombustivelResponseDTO result = service.create(requestDTO);

    // Assert
    assertNotNull(result);
    assertEquals(responseDTO.id(), result.id());
    assertEquals(responseDTO.tipoCombustivel().id(), result.tipoCombustivel().id());

    // Verifica se o serviço realmente buscou o TipoCombustivel
    verify(tipoCombustivelRepository, times(1)).findById(1L);
    verify(bombaRepository, times(1)).save(any(BombaCombustivel.class));
  }

  @Test
  @DisplayName("Deve falhar ao criar Bomba se TipoCombustivel não existir")
  void createShouldThrowResourceNotFoundWhenTipoCombustivelNotExists() {
    // Arrange
    BombaCombustivelRequestDTO badRequest = new BombaCombustivelRequestDTO("Bomba 02", 99L);
    // 1. Simula a busca do TipoCombustivel (que falha)
    when(tipoCombustivelRepository.findById(99L)).thenReturn(Optional.empty());

    // Act & Assert
    assertThrows(ResourceNotFoundException.class, () -> service.create(badRequest),
        "Deveria lançar ResourceNotFoundException");

    // Garante que nada foi salvo
    verify(bombaRepository, never()).save(any());
  }
}
