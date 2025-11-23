package br.com.damasceno.postodecombustivel.service.impl;

import br.com.damasceno.postodecombustivel.dto.AbastecimentoRequestDTO;
import br.com.damasceno.postodecombustivel.dto.AbastecimentoResponseDTO;
import br.com.damasceno.postodecombustivel.exception.BusinessException;
import br.com.damasceno.postodecombustivel.exception.ResourceNotFoundException;
import br.com.damasceno.postodecombustivel.mapper.AbastecimentoMapper;
import br.com.damasceno.postodecombustivel.model.Abastecimento;
import br.com.damasceno.postodecombustivel.model.BombaCombustivel;
import br.com.damasceno.postodecombustivel.model.StatusAbastecimento;
import br.com.damasceno.postodecombustivel.model.TipoCombustivel;
import br.com.damasceno.postodecombustivel.repository.AbastecimentoRepository;
import br.com.damasceno.postodecombustivel.repository.BombaCombustivelRepository;
import br.com.damasceno.postodecombustivel.repository.ClienteRepository;
import br.com.damasceno.postodecombustivel.service.AbastecimentoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AbastecimentoServiceImpl implements AbastecimentoService {

  private static final int LITRAGEM_SCALE = 3;
  private static final int VALOR_TOTAL_SCALE = 2;
  private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;

  private final AbastecimentoRepository abastecimentoRepository;
  private final BombaCombustivelRepository bombaRepository;
  private final AbastecimentoMapper mapper;
  private final ClienteRepository clienteRepository;


  @Override
  @Transactional
  public AbastecimentoResponseDTO create(AbastecimentoRequestDTO requestDTO) {
    log.info("Iniciando criação de abastecimento para bomba ID: {}", requestDTO.bombaId());

    Abastecimento abastecimento = mapper.toEntity(requestDTO);
    abastecimento.setStatus(StatusAbastecimento.CONCLUIDO); // Define o status inicial

    BombaCombustivel bombaCombustivel = findBombaById(requestDTO.bombaId());
    abastecimento.setBombaCombustivel(bombaCombustivel);

    TipoCombustivel tipoCombustivel = bombaCombustivel.getTipoCombustivel();
    BigDecimal precoLitro = tipoCombustivel.getPrecoLitro();

    if (precoLitro == null || precoLitro.compareTo(BigDecimal.ZERO) <= 0) {
      log.warn("Falha ao criar abastecimento: Bomba ID {} não possui preço de combustível válido.",
          bombaCombustivel.getId());
      throw new BusinessException(
          "O tipo de combustível associado à bomba não possui um preço válido.");
    }

    calcularValores(abastecimento, precoLitro);

    // Vincular Cliente se existir
    if (requestDTO.cpfCliente() != null && !requestDTO.cpfCliente().isBlank()) {
      String cpfLimpo = requestDTO.cpfCliente().replaceAll("[^0-9]", "");

      clienteRepository.findByCpf(cpfLimpo).ifPresentOrElse(cliente -> {
        abastecimento.setCliente(cliente);
        log.info("Abastecimento vinculado ao cliente com CPF: {}", cpfLimpo);
      }, () -> log.info("CPF informado ({}) mas cliente não encontrado na base local.", cpfLimpo));

    }
    Abastecimento savedAbastecimento = abastecimentoRepository.save(abastecimento);
    log.info("Abastecimento ID: {} criado com sucesso.", savedAbastecimento.getId());

    return mapper.toResponseDTO(savedAbastecimento);
  }

  private void calcularValores(Abastecimento abastecimento, BigDecimal precoLitro) {
    boolean hasLitragem = abastecimento.getLitragem() != null
        && abastecimento.getLitragem().compareTo(BigDecimal.ZERO) >= 0;
    boolean hasValorTotal = abastecimento.getValorTotal() != null
        && abastecimento.getValorTotal().compareTo(BigDecimal.ZERO) >= 0;

    if (hasLitragem && hasValorTotal) {
      throw new BusinessException("Forneça apenas a litragem ou o valor total, não ambos.");
    }

    if (!hasLitragem && !hasValorTotal) {
      throw new BusinessException("É necessário fornecer a litragem ou o valor total.");
    }

    if (hasLitragem) {
      BigDecimal valorCalculado = abastecimento.getLitragem().multiply(precoLitro)
          .setScale(VALOR_TOTAL_SCALE, ROUNDING_MODE);
      abastecimento.setValorTotal(valorCalculado);
    }

    else {
      BigDecimal litragemCalculada =
          abastecimento.getValorTotal().divide(precoLitro, LITRAGEM_SCALE, ROUNDING_MODE);
      abastecimento.setLitragem(litragemCalculada);
    }
  }

  @Override
  @Transactional(readOnly = true)
  public List<AbastecimentoResponseDTO> findAll() {
    log.debug("Listando todos os abastecimentos.");
    return abastecimentoRepository.findAll().stream().map(mapper::toResponseDTO)
        .collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public AbastecimentoResponseDTO findById(Long id) {
    return mapper.toResponseDTO(findEntityById(id));
  }

  @Override
  @Transactional
  public AbastecimentoResponseDTO cancelar(Long id) {
    log.info("Iniciando cancelamento do abastecimento ID: {}", id);

    Abastecimento abastecimento = findEntityById(id);

    if (abastecimento.getStatus() == StatusAbastecimento.CANCELADO) {
      log.warn("Tentativa de cancelar abastecimento com ID: {} que já esta cancelado.", id);
      throw new BusinessException("Esse abastecimento já se encontra cancelado.");

    }
    abastecimento.setStatus(StatusAbastecimento.CANCELADO);

    Abastecimento abastecimentoCancelado = abastecimentoRepository.save(abastecimento);
    log.info("Abastecimento ID: {} cancelado com sucesso.", abastecimentoCancelado.getId());

    return mapper.toResponseDTO(abastecimentoCancelado);
  }

  private Abastecimento findEntityById(long id) {
    return abastecimentoRepository.findById(id).orElseThrow(() -> {
      log.warn("Abastecimento não encontrado com ID: {}", id);
      return new ResourceNotFoundException("Abastecimento não encontrado com ID: " + id);
    });
  }

  private BombaCombustivel findBombaById(long id) {
    return bombaRepository.findById(id).orElseThrow(() -> {
      log.warn("Bomba de combustível não encontrada com ID: {}", id);
      return new ResourceNotFoundException("Bomba de combustível não encontrada com ID: " + id);
    });
  }
}
