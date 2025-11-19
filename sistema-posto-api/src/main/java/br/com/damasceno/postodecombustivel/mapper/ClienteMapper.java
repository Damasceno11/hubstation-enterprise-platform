package br.com.damasceno.postodecombustivel.mapper;

import br.com.damasceno.postodecombustivel.dto.ClienteCadastroDTO;
import br.com.damasceno.postodecombustivel.dto.ClienteResponseDTO;
import br.com.damasceno.postodecombustivel.model.Cliente;
import org.springframework.stereotype.Component;

@Component
public class ClienteMapper {

    public Cliente toEntity(ClienteCadastroDTO dto) {
        if (dto == null) {
            return null;
        }
        Cliente cliente = new Cliente();
        cliente.setNome(dto.nome());
        cliente.setEmail(dto.email());
        cliente.setCpf(dto.cpf());
        return cliente;
    }

    public ClienteResponseDTO toResponseDTO(Cliente entity) {
        if (entity == null) {
            return null;
        }
        return new  ClienteResponseDTO(
                entity.getId(),
                entity.getNome(),
                entity.getCpf(),
                entity.getEmail()
        );
    }

    public void updateEntityFromDTO(ClienteCadastroDTO dto, Cliente entity) {
        if (dto == null || entity == null) {
            return;
        }
    entity.setNome(dto.nome());
    entity.setEmail(dto.email());
    }
}
