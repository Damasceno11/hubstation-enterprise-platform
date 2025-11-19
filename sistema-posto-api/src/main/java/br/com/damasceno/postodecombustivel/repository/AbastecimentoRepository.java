package br.com.damasceno.postodecombustivel.repository;

import br.com.damasceno.postodecombustivel.model.Abastecimento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AbastecimentoRepository  extends JpaRepository<Abastecimento, Long> {}
