package br.com.damasceno.postodecombustivel.mapper;

import br.com.damasceno.postodecombustivel.dto.BombaCombustivelRequestDTO;
import br.com.damasceno.postodecombustivel.dto.BombaCombustivelResponseDTO;
import br.com.damasceno.postodecombustivel.model.BombaCombustivel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BombaCombustivelMapper {

    private final TipoCombustivelMapper tipoCombustivelMapper;

    public BombaCombustivel toEntity(BombaCombustivelRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        BombaCombustivel bombaCombustivel = new BombaCombustivel();
    bombaCombustivel.setNome(dto.nome());
    return bombaCombustivel;
    }

    public BombaCombustivelResponseDTO toResponseDto(BombaCombustivel entity) {
        if (entity == null) {
            return null;
        }
        return new BombaCombustivelResponseDTO(entity.getId(), entity.getNome(), tipoCombustivelMapper.toResponseDTO(entity.getTipoCombustivel()));
    }

    public void updateEntityFromDTO(BombaCombustivelRequestDTO dto, BombaCombustivel entity) {
        if (dto == null || entity == null) {
            return;
        }
        entity.setNome(dto.nome());
    }
}
