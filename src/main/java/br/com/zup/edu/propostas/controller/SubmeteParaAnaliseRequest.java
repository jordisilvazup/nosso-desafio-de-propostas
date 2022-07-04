package br.com.zup.edu.propostas.controller;

import com.fasterxml.jackson.annotation.JsonProperty;

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

}
