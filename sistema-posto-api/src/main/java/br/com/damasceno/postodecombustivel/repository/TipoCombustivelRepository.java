package br.com.damasceno.postodecombustivel.repository;

import br.com.damasceno.postodecombustivel.model.TipoCombustivel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TipoCombustivelRepository extends JpaRepository<TipoCombustivel, Long> {

    Optional<TipoCombustivel> findByNome(String nome);
}
