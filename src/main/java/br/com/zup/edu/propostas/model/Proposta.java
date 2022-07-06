package br.com.zup.edu.propostas.model;

import br.com.zup.edu.propostas.controller.StatusDaProposta;
import br.com.zup.edu.propostas.jobs.Cartao;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(
        uniqueConstraints = {
                @UniqueConstraint(name = "UK_DOCUMENTO", columnNames = "documento")
        }
)
public class Proposta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String documento;

    @Column(nullable = false)
    private String endereco;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private BigDecimal salario;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime criadaEm;

    /**
     * Migrations:
     *  1. cria coluna NULLABLE
     *  2. UPDATE para definir todas as linhas como `ELEGIVEL`
     *  3. altera coluna para NOT_NULL
     */
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private StatusDaProposta status = StatusDaProposta.NAO_ELEGIVEL;

    @OneToOne
    private Cartao cartao;

    public Proposta(String documento, String endereco, String nome, String email, BigDecimal salario) {
        this.documento = documento;
        this.endereco = endereco;
        this.nome = nome;
        this.email = email;
        this.salario = salario;
    }

    /**
     * @deprecated o construtor é de uso exclusivo do Hibernate
     */
    @Deprecated
    public Proposta() {
    }


    public Long getId() {
        return id;
    }

    public String getDocumento() {
        return documento;
    }

    public String getNome() {
        return nome;
    }

    public LocalDateTime getCriadaEm() {
        return criadaEm;
    }

    public StatusDaProposta getStatus() {
        return status;
    }
    public void setStatus(StatusDaProposta status) {
        this.status = status;
    }

    public void associaAo(Cartao cartao) {
        this.cartao = cartao;
    }

}
