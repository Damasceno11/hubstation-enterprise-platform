package br.com.damasceno.postodecombustivel.controller;

import java.math.BigDecimal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import br.com.damasceno.postodecombustivel.dto.DashboardDTO;
import br.com.damasceno.postodecombustivel.repository.AbastecimentoRepository;
import br.com.damasceno.postodecombustivel.repository.BombaCombustivelRepository;
import br.com.damasceno.postodecombustivel.repository.TipoCombustivelRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@Tag(name = "Dashboard", description = "Dados consolidados para a tela inicial")
public class DashboardController {

  private final TipoCombustivelRepository combustivelRepo;
  private final BombaCombustivelRepository bombaRepo;
  private final AbastecimentoRepository abastecimentoRepo;

  @GetMapping
  public DashboardDTO getStats() {
    long combustiveis = combustivelRepo.count();
    long bombas = bombaRepo.count();
    long abastecimentos = abastecimentoRepo.count();

    // Soma o valor total de todos os abastecimentos (filtro simples)
    // Em um sistema real, faríamos uma query SUM() no banco, mas aqui stream resolve
    BigDecimal totalVendas = abastecimentoRepo.findAll().stream().map(a -> a.getValorTotal())
        .reduce(BigDecimal.ZERO, BigDecimal::add);

    return new DashboardDTO(combustiveis, bombas, abastecimentos, totalVendas);
  }
}
