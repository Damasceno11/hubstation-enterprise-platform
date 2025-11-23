package br.com.damasceno.postodecombustivel.service.impl;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import br.com.damasceno.postodecombustivel.model.Usuario;
import br.com.damasceno.postodecombustivel.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsServiceImpl implements UserDetailsService {

  private final UsuarioRepository repository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Usuario usuario = repository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + username));

    // Agora o User.builder() vai funcionar corretamente pois estamos usando a classe certa do
    // Spring Security
    return User.builder().username(usuario.getUsername()).password(usuario.getPassword())
        .roles(usuario.getRole()).build();
  }
}
