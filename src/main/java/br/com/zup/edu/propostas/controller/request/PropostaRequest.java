package br.com.zup.edu.propostas.controller.request;

import br.com.zup.edu.propostas.model.Proposta;
import br.com.zup.edu.propostas.repository.PropostaRepository;
import br.com.zup.edu.propostas.validation.DocumentoValido;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

public class PropostaRequest {

    @NotBlank
//    @DocumentoValido
    private String documento;

    @NotBlank
    private String endereco;

    @NotBlank
    private String nome;

    @NotBlank
    @Email
    private String email;

    @NotNull
    @Positive
    private BigDecimal salario;


    public PropostaRequest(String documento, String endereco, String nome, String email, BigDecimal salario) {
        this.documento = documento;
        this.endereco = endereco;
        this.nome = nome;
        this.email = email;
        this.salario = salario;
    }

    public Proposta toModel(PropostaRepository repository){

        // The Pastel's Law = Lei da Robustez
        //    - seja liberal no que vc recebe
        //    - seja conservador no que vc envia

        String documentoSemMascara = documento.replaceAll("[^0-9]", "");
        if(repository.existsByDocumento(documentoSemMascara)) {
            throw new ResponseStatusException(UNPROCESSABLE_ENTITY,"Já possui uma proposta para este documento");
        }

        return new Proposta(documentoSemMascara,endereco,nome,email,salario);
    }

    public String getDocumento() {
        return documento;
    }

    public String getEndereco() {
        return endereco;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public BigDecimal getSalario() {
        return salario;
    }
}
