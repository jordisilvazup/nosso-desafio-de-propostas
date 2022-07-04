package br.com.zup.edu.propostas.controller;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class SubmeteParaAnaliseRequest {

    @JsonProperty("idProposta")
    private Long propostaId;

    private String documento;
    private String nome;

    public SubmeteParaAnaliseRequest(Long propostaId, String documento, String nome) {
        this.propostaId = propostaId;
        this.documento = documento;
        this.nome = nome;
    }

    public Long getPropostaId() {
        return propostaId;
    }

    public String getDocumento() {
        return documento;
    }

    public String getNome() {
        return nome;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubmeteParaAnaliseRequest that = (SubmeteParaAnaliseRequest) o;
        return Objects.equals(documento, that.documento)
                && Objects.equals(nome, that.nome);
    }

    @Override
    public int hashCode() {
        return Objects.hash(documento, nome);
    }
}
