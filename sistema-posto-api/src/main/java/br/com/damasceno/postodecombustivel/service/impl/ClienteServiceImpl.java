package br.com.damasceno.postodecombustivel.service.impl;

import br.com.damasceno.postodecombustivel.config.RabbitMQConfig;
import br.com.damasceno.postodecombustivel.dto.ClienteCadastroDTO;
import br.com.damasceno.postodecombustivel.dto.ClienteResponseDTO;
import br.com.damasceno.postodecombustivel.dto.NotificacaoDTO;
import br.com.damasceno.postodecombustivel.exception.BusinessException;
import br.com.damasceno.postodecombustivel.mapper.ClienteMapper;
import br.com.damasceno.postodecombustivel.model.Cliente;
import br.com.damasceno.postodecombustivel.repository.ClienteRepository;
import br.com.damasceno.postodecombustivel.service.ClienteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository repository;
    private final ClienteMapper mapper;
    private final RabbitTemplate rabbitTemplate;

    @Override
    @Transactional
    public ClienteResponseDTO cadastrar(ClienteCadastroDTO dto) {
        log.info("Processando cadastro de cliente: {}", dto.cpf());

        // Normaliza CPF para salvar sem pontos/traços
        String cpfLimpo = dto.cpf().replaceAll("[^0-9]", "");

        // Regra de Negócio: O E-mail já está em uso por OUTRO CPF?
        Optional<Cliente> porEmail = repository.findByEmail(dto.email());
        if (porEmail.isPresent() && !porEmail.get().getCpf().equals(cpfLimpo)) {
            throw new BusinessException("Este e-mail já está sendo usado por outro cliente.");
        }

        // Regra de Negócio: Cliente já existe pelo CPF?
        Optional<Cliente> porCpf = repository.findByCpf(cpfLimpo);
        Cliente cliente;

        if (porCpf.isPresent()) {
            log.info("Cliente (CPF: {}) já existe. Atualizando e-mail para fidelidade.", cpfLimpo);
            cliente = porCpf.get();
            mapper.updateEntityFromDTO(dto, cliente);
        } else {
            log.info("Novo cliente (CPF: {}). Criando registro.", cpfLimpo);
            cliente = mapper.toEntity(dto);
            cliente.setCpf(cpfLimpo); // Garante CPF limpo
        }

        Cliente clienteSalvo = repository.save(cliente);

        // Dispara a mensagem para o RabbitMQ
        NotificacaoDTO notificacao = new NotificacaoDTO(clienteSalvo.getNome(), clienteSalvo.getEmail());
        try {
            rabbitTemplate.convertAndSend(RabbitMQConfig.FIDELIDADE_QUEUE, notificacao);
            log.info("Mensagem de fidelidade enviada para [{}].", RabbitMQConfig.FIDELIDADE_QUEUE);
        } catch (Exception e) {
            log.error("ERRO ao enviar mensagem para o RabbitMQ: {}", e.getMessage());
        }

        return mapper.toResponseDTO(clienteSalvo);
    }
}
