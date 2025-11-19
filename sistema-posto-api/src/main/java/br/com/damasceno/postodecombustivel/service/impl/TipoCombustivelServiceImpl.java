package br.com.damasceno.postodecombustivel.service.impl;

import br.com.damasceno.postodecombustivel.dto.TipoCombustivelRequestDTO;
import br.com.damasceno.postodecombustivel.dto.TipoCombustivelResponseDTO;
import br.com.damasceno.postodecombustivel.exception.BusinessException;
import br.com.damasceno.postodecombustivel.exception.ResourceNotFoundException;
import br.com.damasceno.postodecombustivel.mapper.TipoCombustivelMapper;
import br.com.damasceno.postodecombustivel.model.TipoCombustivel;
import br.com.damasceno.postodecombustivel.repository.TipoCombustivelRepository;
import br.com.damasceno.postodecombustivel.service.TipoCombustivelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TipoCombustivelServiceImpl implements TipoCombustivelService {

    private final TipoCombustivelRepository repository;
    private final TipoCombustivelMapper mapper;

    @Override
    @Transactional
    public TipoCombustivelResponseDTO create(TipoCombustivelRequestDTO requestDTO) {
        log.info("Criando tipo combustível com nome: {}", requestDTO.nome());
        repository.findByNome(requestDTO.nome())
                .ifPresent(
                        (entity) -> {
                            log.warn("Tentativa de criar TipoCombustivel com nome duplicado: {}", requestDTO.nome());
                            throw new BusinessException(
                                    "Já existe um tipo de combustível com o nome: " + requestDTO.nome());
                        });

        TipoCombustivel entity = mapper.toEntity(requestDTO);
        TipoCombustivel savedEntity = repository.save(entity);
        log.info("Tipo combustível com ID: {} criado com sucesso", savedEntity.getId());
        return mapper.toResponseDTO(savedEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TipoCombustivelResponseDTO> findAll() {
        log.debug("Listando todos os Tipos de Combustível");
        return repository.findAll().stream()
                .map(mapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public TipoCombustivelResponseDTO findById(Long id) {
        TipoCombustivel entity = findEntityById(id);
        return mapper.toResponseDTO(entity);
    }

    @Override
    @Transactional
    public TipoCombustivelResponseDTO update(Long id, TipoCombustivelRequestDTO requestDTO) {
        log.info("Atualizando TipoCombustivel ID: {}", id);
        TipoCombustivel entity = findEntityById(id);

        repository.findByNome(requestDTO.nome())
                .ifPresent(
                        (existingEntity) -> {
                            if (!existingEntity.getId().equals(id)) {
                                log.warn("Conflito de nome ao atualizar ID: {}. Nome '{}' já usado pelo ID: {}", id, requestDTO.nome(), existingEntity.getId());
                                throw new BusinessException(
                                        "O nome '" + requestDTO.nome() + "' já está em uso por outro combustível.");
                            }
                        });

        mapper.updateEntityFromDTO(requestDTO, entity);
        TipoCombustivel  updatedEntity = repository.save(entity);
        log.info("TipoCombustivel ID: {} atualizado com sucesso.", updatedEntity.getId());
        return mapper.toResponseDTO(updatedEntity);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        log.info("Deletando TipoCombustivel ID: {}", id);
        findEntityById(id);
        repository.deleteById(id);
        log.info("TipoCombustivel ID: {} deletado com sucesso.", id);
    }

    private TipoCombustivel findEntityById(Long id) {
        return repository.findById(id).orElseThrow(
                () -> {
                    log.warn("TipoCombustivel não encontrado com ID: {}", id);
                    return new ResourceNotFoundException("Tipo de Combustível não encontrado com ID: " + id);
                });
    }
}
