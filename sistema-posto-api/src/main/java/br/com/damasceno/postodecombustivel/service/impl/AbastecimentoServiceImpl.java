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
  private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;

  private final AbastecimentoRepository abastecimentoRepository;
  private final BombaCombustivelRepository bombaRepository;
  private final AbastecimentoMapper mapper;
  private final ClienteRepository clienteRepository;

  @Override
  @Transactional
  public AbastecimentoResponseDTO create(AbastecimentoRequestDTO requestDTO) {
    // Loga o valor financeiro recebido
    log.info("Iniciando abastecimento na bomba ID: {} - Valor: R$ {}", requestDTO.bombaId(),
        requestDTO.valorTotal());

    // 1. Mapeamento inicial (DTO traz o Valor Total)
    Abastecimento abastecimento = mapper.toEntity(requestDTO);
    abastecimento.setStatus(StatusAbastecimento.CONCLUIDO);

    // 2. Busca e Associa a Bomba
    BombaCombustivel bomba = findBombaById(requestDTO.bombaId());
    abastecimento.setBombaCombustivel(bomba); // Nome do campo na entidade é 'bomba'

    // 3. Valida o Preço do Combustível
    TipoCombustivel tipoCombustivel = bomba.getTipoCombustivel();
    BigDecimal precoLitro = tipoCombustivel.getPrecoLitro();

    if (precoLitro == null || precoLitro.compareTo(BigDecimal.ZERO) <= 0) {
      log.warn("Falha: Bomba ID {} tem combustível com preço inválido.", bomba.getId());
      throw new BusinessException(
          "O tipo de combustível associado à bomba não possui um preço válido.");
    }

    // 4. Lógica de Cálculo Inversa (Litros = Valor / Preço)
    // Ex: R$ 50,00 / R$ 5,00 = 10 Litros
    if (abastecimento.getValorTotal() != null
        && abastecimento.getValorTotal().compareTo(BigDecimal.ZERO) > 0) {
      BigDecimal litragemCalculada =
          abastecimento.getValorTotal().divide(precoLitro, LITRAGEM_SCALE, ROUNDING_MODE);

      abastecimento.setLitragem(litragemCalculada);
    } else {
      throw new BusinessException("O valor total do abastecimento deve ser maior que zero.");
    }

    // 5. Vincula Cliente (Fidelidade) se CPF for informado
    if (requestDTO.cpfCliente() != null && !requestDTO.cpfCliente().isBlank()) {
      String cpfLimpo = requestDTO.cpfCliente().replaceAll("[^0-9]", "");

      clienteRepository.findByCpf(cpfLimpo).ifPresentOrElse(cliente -> {
        abastecimento.setCliente(cliente);
        log.info("Abastecimento vinculado ao cliente: {} (CPF: {})", cliente.getNome(), cpfLimpo);
      }, () -> log.info("CPF informado ({}) não encontrado. Abastecimento seguirá como anônimo.",
          cpfLimpo));
    }

    // 6. Salva e Retorna
    Abastecimento savedAbastecimento = abastecimentoRepository.save(abastecimento);
    log.info("Abastecimento ID: {} registrado com sucesso. Litragem calculada: {} L",
        savedAbastecimento.getId(), savedAbastecimento.getLitragem());

    return mapper.toResponseDTO(savedAbastecimento);
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
      log.warn("Tentativa de cancelar abastecimento ID: {} que já estava cancelado.", id);
      throw new BusinessException("Esse abastecimento já se encontra cancelado.");
    }

    abastecimento.setStatus(StatusAbastecimento.CANCELADO);
    Abastecimento abastecimentoCancelado = abastecimentoRepository.save(abastecimento);

    log.info("Abastecimento ID: {} cancelado com sucesso.", abastecimentoCancelado.getId());

    return mapper.toResponseDTO(abastecimentoCancelado);
  }

  private Abastecimento findEntityById(Long id) {
    return abastecimentoRepository.findById(id).orElseThrow(() -> {
      log.warn("Abastecimento não encontrado com ID: {}", id);
      return new ResourceNotFoundException("Abastecimento não encontrado com ID: " + id);
    });
  }

  private BombaCombustivel findBombaById(Long id) {
    return bombaRepository.findById(id).orElseThrow(() -> {
      log.warn("Bomba não encontrada com ID: {}", id);
      return new ResourceNotFoundException("Bomba de combustível não encontrada com ID: " + id);
    });
  }
}
