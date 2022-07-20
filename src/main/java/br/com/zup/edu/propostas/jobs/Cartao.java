package br.com.zup.edu.propostas.jobs;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class Cartao {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cartao_seq")
    @SequenceGenerator(
        name = "cartao_seq",
        sequenceName = "cartao_seq",
        initialValue = 1,
        allocationSize = 1
    )
    private Long id;

    @Column(nullable = false, unique = true)
    private String numero;

    @Column(nullable = false)
    private String titular;

    @Column(nullable = false)
    private BigDecimal limite;

    @Column(nullable = false)
    private LocalDateTime emitidoEm;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @Deprecated
    public Cartao(){}

    public Cartao(String numero, String titular, BigDecimal limite, LocalDateTime emitidoEm) {
        this.numero = numero;
        this.titular = titular;
        this.limite = limite;
        this.emitidoEm = emitidoEm;
    }

    public Long getId() {
        return id;
    }

    public String getNumero() {
        return numero;
    }

    public String getTitular() {
        return titular;
    }

    public BigDecimal getLimite() {
        return limite;
    }

    public LocalDateTime getEmitidoEm() {
        return emitidoEm;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }
}
