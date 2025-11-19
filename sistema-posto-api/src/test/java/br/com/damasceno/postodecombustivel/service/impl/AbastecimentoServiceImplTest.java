package br.com.damasceno.postodecombustivel.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import br.com.damasceno.postodecombustivel.dto.AbastecimentoRequestDTO;
import br.com.damasceno.postodecombustivel.dto.AbastecimentoResponseDTO;
import br.com.damasceno.postodecombustivel.exception.BusinessException;
import br.com.damasceno.postodecombustivel.mapper.AbastecimentoMapper;
import br.com.damasceno.postodecombustivel.model.Abastecimento;
import br.com.damasceno.postodecombustivel.model.BombaCombustivel;
import br.com.damasceno.postodecombustivel.model.StatusAbastecimento;
import br.com.damasceno.postodecombustivel.model.TipoCombustivel;
import br.com.damasceno.postodecombustivel.repository.AbastecimentoRepository;
import br.com.damasceno.postodecombustivel.repository.BombaCombustivelRepository;
import br.com.damasceno.postodecombustivel.repository.ClienteRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AbastecimentoServiceImplTest {

    @Mock private AbastecimentoRepository abastecimentoRepository;
    @Mock private BombaCombustivelRepository bombaRepository;
    @Mock private ClienteRepository clienteRepository; // NOVIDADE: Mock necessário
    @Mock private AbastecimentoMapper mapper;

    @InjectMocks private AbastecimentoServiceImpl service;

    private TipoCombustivel tipoCombustivel;
    private BombaCombustivel bombaCombustivel;
    private Abastecimento abastecimentoConcluido;

    @BeforeEach
    void setUp() {
        tipoCombustivel = new TipoCombustivel(1L, "Gasolina Comum", new BigDecimal("5.00"));
        bombaCombustivel = new BombaCombustivel(1L, "Bomba 01", tipoCombustivel);

        // Abastecimento Concluído (Simulação do Banco)
        abastecimentoConcluido =
                new Abastecimento(
                        1L,
                        LocalDate.now(),
                        new BigDecimal("10.000"),
                        new BigDecimal("50.00"),
                        StatusAbastecimento.CONCLUIDO,
                        bombaCombustivel,
                        null); // Cliente é null (anônimo) neste cenário base
    }

    @Test
    @DisplayName("Deve criar Abastecimento calculando valorTotal (dada a litragem)")
    void createShouldCalculateValorTotalWhenLitragemIsProvided() {
        // Arrange: DTO com CPF null
        AbastecimentoRequestDTO request =
                new AbastecimentoRequestDTO(1L, LocalDate.now(), new BigDecimal("10.000"), null);

        // Entidade antes de salvar (sem IDs, sem cliente)
        Abastecimento entityPreCalculo =
                new Abastecimento(null, LocalDate.now(), new BigDecimal("10.000"), null, null, null, null);

        when(bombaRepository.findById(1L)).thenReturn(Optional.of(bombaCombustivel));
        when(mapper.toEntity(request)).thenReturn(entityPreCalculo);
        when(abastecimentoRepository.save(any(Abastecimento.class))).thenReturn(abastecimentoConcluido);
        when(mapper.toResponseDTO(abastecimentoConcluido)).thenReturn(mock(AbastecimentoResponseDTO.class));

        // Act
        service.create(request);

        // Assert
        // Verifica se o cálculo foi feito (10 * 5.00 = 50.00)
        assertEquals(0, new BigDecimal("50.00").compareTo(entityPreCalculo.getValorTotal()));
        assertEquals(StatusAbastecimento.CONCLUIDO, entityPreCalculo.getStatus());
        verify(abastecimentoRepository, times(1)).save(entityPreCalculo);
    }

    @Test
    @DisplayName("Deve criar Abastecimento calculando litragem (dado o valorTotal)")
    void createShouldCalculateLitragemWhenValorTotalIsProvided() {
        // Arrange: DTO com litragem null e valorTotal preenchido
        AbastecimentoRequestDTO request =
                new AbastecimentoRequestDTO(1L, LocalDate.now(), null, null);

        Abastecimento entityPreCalculo =
                new Abastecimento(null, LocalDate.now(), null, new BigDecimal("50.00"), null, null, null);

        when(bombaRepository.findById(1L)).thenReturn(Optional.of(bombaCombustivel));
        when(mapper.toEntity(request)).thenReturn(entityPreCalculo);
        when(abastecimentoRepository.save(any(Abastecimento.class))).thenReturn(abastecimentoConcluido);
        when(mapper.toResponseDTO(abastecimentoConcluido)).thenReturn(mock(AbastecimentoResponseDTO.class));

        // Act
        service.create(request);

        // Assert
        // Verifica se o cálculo foi feito (50.00 / 5.00 = 10.000)
        assertEquals(0, new BigDecimal("10.000").compareTo(entityPreCalculo.getLitragem()));
        verify(abastecimentoRepository, times(1)).save(entityPreCalculo);
    }

    @Test
    @DisplayName("Deve falhar ao criar se litragem e valorTotal forem nulos")
    void createShouldThrowBusinessExceptionWhenBothAreNull() {
        // Arrange
        AbastecimentoRequestDTO request =
                new AbastecimentoRequestDTO(1L, LocalDate.now(), null, null);
        Abastecimento entityPreCalculo = new Abastecimento();

        when(bombaRepository.findById(1L)).thenReturn(Optional.of(bombaCombustivel));
        when(mapper.toEntity(request)).thenReturn(entityPreCalculo);

        // Act & Assert
        assertThrows(BusinessException.class, () -> service.create(request));
        verify(abastecimentoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve falhar ao criar se litragem e valorTotal forem preenchidos (ambiguidade)")
    void createShouldThrowBusinessExceptionWhenBothAreProvided() {
        // Arrange
        AbastecimentoRequestDTO request =
                new AbastecimentoRequestDTO(1L, LocalDate.now(), new BigDecimal("10"), null);
        Abastecimento entityPreCalculo =
                new Abastecimento(null, LocalDate.now(), new BigDecimal("10"), new BigDecimal("50"), null, null, null);

        when(bombaRepository.findById(1L)).thenReturn(Optional.of(bombaCombustivel));
        when(mapper.toEntity(request)).thenReturn(entityPreCalculo);

        // Act & Assert
        assertThrows(BusinessException.class, () -> service.create(request));
        verify(abastecimentoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve cancelar um abastecimento com sucesso")
    void cancelarShouldUpdateStatus() {
        // Arrange
        when(abastecimentoRepository.findById(1L)).thenReturn(Optional.of(abastecimentoConcluido));
        when(abastecimentoRepository.save(any(Abastecimento.class))).thenReturn(abastecimentoConcluido);
        when(mapper.toResponseDTO(abastecimentoConcluido)).thenReturn(mock(AbastecimentoResponseDTO.class));

        // Act
        service.cancelar(1L);

        // Assert
        assertEquals(StatusAbastecimento.CANCELADO, abastecimentoConcluido.getStatus());
        verify(abastecimentoRepository, times(1)).save(abastecimentoConcluido);
    }

    @Test
    @DisplayName("Deve falhar ao cancelar um abastecimento já cancelado")
    void cancelarShouldThrowBusinessExceptionWhenAlreadyCanceled() {
        // Arrange
        abastecimentoConcluido.setStatus(StatusAbastecimento.CANCELADO);
        when(abastecimentoRepository.findById(1L)).thenReturn(Optional.of(abastecimentoConcluido));

        // Act & Assert
        assertThrows(BusinessException.class, () -> service.cancelar(1L));
        verify(abastecimentoRepository, never()).save(any());
    }
}