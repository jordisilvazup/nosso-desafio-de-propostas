package br.com.zup.edu.propostas.model;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
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
}
