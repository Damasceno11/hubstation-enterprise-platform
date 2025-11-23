package br.com.damasceno.postodecombustivel.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import br.com.damasceno.postodecombustivel.model.Usuario;
import br.com.damasceno.postodecombustivel.repository.UsuarioRepository;

@Configuration
public class DataInitializer {

  @Bean
  CommandLineRunner initDatabase(UsuarioRepository usuarioRepository,
      PasswordEncoder passwordEncoder) {
    return args -> {
      // Verifica se o admin já existe
      if (usuarioRepository.findByUsername("admin").isEmpty()) {
        Usuario admin = new Usuario();
        admin.setUsername("admin");
        // Criptografa a senha "123456"
        admin.setPassword(passwordEncoder.encode("123456"));
        admin.setRole("ADMIN");

        usuarioRepository.save(admin);
        System.out.println("✅ SEEDER: Usuário ADMIN criado com sucesso (admin/123456)");
      }
    };
  }
}
