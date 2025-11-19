package br.com.damasceno.notificacao.consumer;

import br.com.damasceno.notificacao.dto.NotificacaoDTO;
import br.com.damasceno.notificacao.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class FidelidadeConsumer {

    private final EmailService emailService;

    public static final String FIDELIDADE_QUEUE = "fidelidade.v1.novo-cliente";

    @RabbitListener(queues = FIDELIDADE_QUEUE)
    public void onNovoCliente(NotificacaoDTO dto) {
        log.info("Mensagem recebida da fila [{}]: {}", FIDELIDADE_QUEUE, dto);

        emailService.enviarEmailDeBoasVindas(dto);
    }
}
