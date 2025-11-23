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
    // 1. Proteção contra nulos
    if (dto.email() == null || dto.email().isBlank()) {
      log.error("Cancelando envio: E-mail está vazio ou nulo.");
      return;
    }

    // 2. Limpeza de espaços em branco (A CORREÇÃO PRINCIPAL)
    String emailDestino = dto.email().trim();

    log.info("Preparando envio de e-mail para: [{}]", emailDestino);

    try {
      SimpleMailMessage message = new SimpleMailMessage();
      message.setFrom("no-reply@hubstation.com.br");

      message.setTo(emailDestino);

      message.setSubject("Bem-vindo ao HubStation Fidelidade! ⛽");

      String texto = String.format(
          """
              Olá %s,

              Seu cadastro no HubStation Fidelidade foi confirmado com sucesso!

              A partir de agora, cada abastecimento gera pontos que podem ser trocados por energia, serviços ou produtos.

              🎁 SEU PRESENTE DE BOAS-VINDAS:
              ------------------------------------------------
              [ QR CODE: CAFÉ EXPRESSO GRÁTIS NA LOJA DE CONVENIÊNCIA ]
              ------------------------------------------------
              Apresente este e-mail no caixa.

              Obrigado por escolher o HubStation.
              Tecnologia e Energia em um só lugar.

              Atenciosamente,
              Equipe HubStation.
              """,
          dto.nome());

      message.setText(texto);
      // Envia para o MailHog
      mailSender.send(message);
      log.info("E-mail enviado com sucesso para MailHog.");

    } catch (Exception e) {
      log.error("FALHA ao enviar e-mail para [{}]: {}", emailDestino, e.getMessage());
      e.printStackTrace();
    }
  }
}


