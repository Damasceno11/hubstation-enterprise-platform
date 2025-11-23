package br.com.damasceno.postodecombustivel.dto;

import java.math.BigDecimal;

public record DashboardDTO(long qtdCombustiveis, long qtdBombas, long qtdAbastecimentos,
    BigDecimal valorTotalVendas) {

}
