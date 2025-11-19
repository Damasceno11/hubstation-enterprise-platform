package br.com.damasceno.postodecombustivel.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "abastecimento")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Abastecimento {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "abastecimento_seq")
    @SequenceGenerator(
            name = "abastecimento_seq",
            sequenceName = "abastecimento_id_seq",
            allocationSize = 50)
    private Long id;

    @Column(name = "data_abastecimento", nullable = false)
    private LocalDate dataAbastecimento;

    @Column(name = "litragem", nullable = false, precision = 10, scale = 3)
    private BigDecimal litragem;

    @Column(name = "valor_total", nullable = false, precision = 10, scale = 2)
    private BigDecimal valorTotal;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private StatusAbastecimento status = StatusAbastecimento.CONCLUIDO;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bomba_id", nullable = false)
    private BombaCombustivel bombaCombustivel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Abastecimento that = (Abastecimento) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
