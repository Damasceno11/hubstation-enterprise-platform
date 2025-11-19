package br.com.damasceno.postodecombustivel.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.swing.plaf.basic.BasicIconFactory;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "tipo_combustivel")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TipoCombustivel {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tipo_combustivel_seq")
    @SequenceGenerator(
            name = "tipo_combustivel_seq",
            sequenceName = "tipo_combustivel_id_seq",
            allocationSize = 50)
    private Long id;

    @Column(name = "nome",  nullable = false, length = 100,  unique = true)
    private String nome;

    @Column(name = "preco_litro", nullable = false, precision = 10, scale = 2)
    private BigDecimal precoLitro;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TipoCombustivel that = (TipoCombustivel) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
