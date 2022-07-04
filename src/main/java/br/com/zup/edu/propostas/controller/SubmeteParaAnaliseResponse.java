package br.com.zup.edu.propostas.controller;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SubmeteParaAnaliseResponse {

    @JsonProperty("idProposta")
    private Long propostaId;

    private String documento;
    private String nome;

    private String resultadoSolicitacao;

    public SubmeteParaAnaliseResponse(Long propostaId, String documento, String nome, String resultadoSolicitacao) {
        this.propostaId = propostaId;
        this.documento = documento;
        this.nome = nome;
        this.resultadoSolicitacao = resultadoSolicitacao;
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

    public String getResultadoSolicitacao() {
        return resultadoSolicitacao;
    }
}
