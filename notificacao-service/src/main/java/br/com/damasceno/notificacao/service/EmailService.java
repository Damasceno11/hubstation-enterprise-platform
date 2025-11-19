package br.com.damasceno.notificacao.service;

import br.com.damasceno.notificacao.dto.NotificacaoDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    public void enviarEmailDeBoasVindas(NotificacaoDTO dto) {
        log.info("Recebida solicitação de e-mail para: {}", dto.email());

        try{
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("hub-br@postofidelidade.com");
            message.setTo(dto.email());
            message.setSubject("Bem-vindo ao Hub BR Fidelidade!");

            String texto = String.format("""
                Olá %s,
                
                Seu cadastro no Hub BR Fidelidade foi um sucesso!
                
                Para comemorar, aqui está seu primeiro benefício:
                [ Simulação de QR Code: APRESENTE ESTE E-MAIL E GANHE 1 CAFÉ GRÁTIS ]
                
                Obrigado,
                Equipe Hub BR.
                """, dto.nome());

            message.setText(texto);
            // Envia para o MailHog
            mailSender.send(message);
            log.info("E-mail enviado com sucesso para MailHog.");
        }catch (Exception e){
            log.error("Erro ao tentar enviar e-mail: {}", e.getMessage());
        }
    }
}






















