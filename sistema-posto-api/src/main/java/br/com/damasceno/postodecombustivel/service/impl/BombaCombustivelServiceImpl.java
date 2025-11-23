package br.com.damasceno.postodecombustivel.service.impl;

import br.com.damasceno.postodecombustivel.dto.BombaCombustivelRequestDTO;
import br.com.damasceno.postodecombustivel.dto.BombaCombustivelResponseDTO;
import br.com.damasceno.postodecombustivel.exception.ResourceNotFoundException;
import br.com.damasceno.postodecombustivel.mapper.BombaCombustivelMapper;
import br.com.damasceno.postodecombustivel.model.BombaCombustivel;
import br.com.damasceno.postodecombustivel.model.TipoCombustivel;
import br.com.damasceno.postodecombustivel.repository.BombaCombustivelRepository;
import br.com.damasceno.postodecombustivel.repository.TipoCombustivelRepository;
import br.com.damasceno.postodecombustivel.service.BombaCombustivelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BombaCombustivelServiceImpl implements BombaCombustivelService {

  private final BombaCombustivelRepository bombaRepository;
  private final BombaCombustivelMapper mapper;
  private final TipoCombustivelRepository tipoCombustivelRepository;


  @Override
  @Transactional
  public BombaCombustivelResponseDTO create(BombaCombustivelRequestDTO requestDTO) {
    log.info("Criando Bomba de Combustível: {}", requestDTO.nome());

    TipoCombustivel tipoCombustivel = findTipoCombustivelById(requestDTO.tipoCombustivelId());

    BombaCombustivel bombaCombustivel = mapper.toEntity(requestDTO);

    bombaCombustivel.setTipoCombustivel(tipoCombustivel);

    BombaCombustivel savedBomba = bombaRepository.save(bombaCombustivel);
    log.info("Bomba ID: {} criada com sucesso.", savedBomba.getId());
    return mapper.toResponseDto(savedBomba);
  }

  @Override
  @Transactional(readOnly = true)
  public List<BombaCombustivelResponseDTO> findAll() {
    log.debug("Listando todas as Bombas");
    return bombaRepository.findAll().stream().map(mapper::toResponseDto)
        .collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public BombaCombustivelResponseDTO findById(Long id) {
    log.debug("Buscando Bomba por ID: {}", id);
    return mapper.toResponseDto(findEntityById(id));
  }

  @Override
  @Transactional
  public BombaCombustivelResponseDTO update(Long id, BombaCombustivelRequestDTO requestDTO) {
    log.info("Atualizando Bomba ID: {}", id);

    BombaCombustivel bombaCombustivel = findEntityById(id);
    TipoCombustivel tipoCombustivel = findTipoCombustivelById(requestDTO.tipoCombustivelId());

    mapper.updateEntityFromDTO(requestDTO, bombaCombustivel);
    bombaCombustivel.setTipoCombustivel(tipoCombustivel);

    BombaCombustivel updatedBomba = bombaRepository.save(bombaCombustivel);
    log.info("Bomba ID: {} atualizada com sucesso.", updatedBomba.getId());

    return mapper.toResponseDto(updatedBomba);
  }

  @Override
  @Transactional
  public void delete(Long id) {
    log.info("Deletando Bomba ID: {}", id);
    findEntityById(id);
    bombaRepository.deleteById(id);
    log.info("Bomba ID: {} deletada com sucesso.", id);

  }

  private BombaCombustivel findEntityById(Long id) {
    return bombaRepository.findById(id).orElseThrow(() -> {
      log.warn("Bomba de Combustível não encontrada com ID: {}", id);
      return new ResourceNotFoundException("Bomba de Combustível não encontrada com ID: " + id);
    });
  }

  private TipoCombustivel findTipoCombustivelById(Long id) {
    return tipoCombustivelRepository.findById(id).orElseThrow(() -> {
      log.warn("Tipo de combustivel não encontrada com ID: {}", id);
      return new ResourceNotFoundException("Tipo de combustivel não encontrada com ID: " + id);
    });
  }
}
