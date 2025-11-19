package br.com.damasceno.postodecombustivel.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Entity
@Table(name = "bomba_combustivel")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BombaCombustivel {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bomba_combustivel_seq")
    @SequenceGenerator(
            name = "bomba_combustivel_seq",
            sequenceName = "bomba_combustivel_id_seq",
            allocationSize = 50)
    private Long id;

    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tipo_combustivel_id", nullable = false)
    private TipoCombustivel tipoCombustivel;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BombaCombustivel that = (BombaCombustivel) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
