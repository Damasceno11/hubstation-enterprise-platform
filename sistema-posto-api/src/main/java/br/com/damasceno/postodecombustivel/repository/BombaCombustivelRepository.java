package br.com.damasceno.postodecombustivel.repository;

import br.com.damasceno.postodecombustivel.model.BombaCombustivel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BombaCombustivelRepository extends JpaRepository<BombaCombustivel, Long> {}
