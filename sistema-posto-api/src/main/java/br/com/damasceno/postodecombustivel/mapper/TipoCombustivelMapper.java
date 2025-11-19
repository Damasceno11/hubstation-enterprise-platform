package br.com.damasceno.postodecombustivel.mapper;

import br.com.damasceno.postodecombustivel.dto.TipoCombustivelRequestDTO;
import br.com.damasceno.postodecombustivel.dto.TipoCombustivelResponseDTO;
import br.com.damasceno.postodecombustivel.model.TipoCombustivel;
import org.springframework.stereotype.Component;

@Component
public class TipoCombustivelMapper {

    public TipoCombustivel toEntity(TipoCombustivelRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        return new TipoCombustivel(null, dto.nome(), dto.precoLitro());
    }

    public TipoCombustivelResponseDTO toResponseDTO(TipoCombustivel entity) {
        if (entity == null) {
            return null;
        }
        return new TipoCombustivelResponseDTO(entity.getId(), entity.getNome(), entity.getPrecoLitro());
    }

    public void updateEntityFromDTO(TipoCombustivelRequestDTO dto, TipoCombustivel entity) {
        if (dto == null || entity == null) {
            return;
        }
        entity.setNome(dto.nome());
        entity.setPrecoLitro(dto.precoLitro());
    }
}
