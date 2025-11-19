package br.com.damasceno.postodecombustivel.mapper;


import br.com.damasceno.postodecombustivel.dto.AbastecimentoRequestDTO;
import br.com.damasceno.postodecombustivel.dto.AbastecimentoResponseDTO;
import br.com.damasceno.postodecombustivel.model.Abastecimento;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AbastecimentoMapper {

    private final BombaCombustivelMapper bombaCombustivelMapper;
    private final ClienteMapper clienteMapper;

    public Abastecimento toEntity(AbastecimentoRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        Abastecimento abastecimento = new Abastecimento();
        abastecimento.setDataAbastecimento(dto.dataAbastecimento());
        abastecimento.setLitragem(dto.litragem());
        return abastecimento;
    }

    public AbastecimentoResponseDTO toResponseDTO(Abastecimento entity) {
        if (entity == null) {
            return null;
        }
        return new AbastecimentoResponseDTO(
                entity.getId(),
                entity.getDataAbastecimento(),
                entity.getLitragem(),
                entity.getValorTotal(),
                bombaCombustivelMapper.toResponseDto(entity.getBombaCombustivel()),
                entity.getStatus(),
                clienteMapper.toResponseDTO(entity.getCliente())
                );
    }
}
