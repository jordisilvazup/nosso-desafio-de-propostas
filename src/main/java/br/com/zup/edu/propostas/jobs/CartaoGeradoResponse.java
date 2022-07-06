package br.com.zup.edu.propostas.jobs;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CartaoGeradoResponse {

    @JsonProperty("id")
    private String numeroDoCartao;

    private String titular;
    private LocalDateTime emitidoEm;
    private BigDecimal limite;

    @JsonProperty("idProposta")
    private String propostaId;

    public CartaoGeradoResponse(String numeroDoCartao, String titular, LocalDateTime emitidoEm, BigDecimal limite, String propostaId) {
        this.numeroDoCartao = numeroDoCartao;
        this.titular = titular;
        this.emitidoEm = emitidoEm;
        this.limite = limite;
        this.propostaId = propostaId;
    }

    public String getNumeroDoCartao() {
        return numeroDoCartao;
    }

    public String getTitular() {
        return titular;
    }

    public LocalDateTime getEmitidoEm() {
        return emitidoEm;
    }

    public BigDecimal getLimite() {
        return limite;
    }

    public String getPropostaId() {
        return propostaId;
    }

    @Override
    public String toString() {
        return "CartaoGeradoResponse{" +
                "numeroDoCartao(id)='" + numeroDoCartao + '\'' +
                ", titular='" + titular + '\'' +
                ", emitidoEm=" + emitidoEm +
                ", limite=" + limite +
                ", propostaId='" + propostaId + '\'' +
                '}';
    }

    public Cartao toModel() {
        return new Cartao(numeroDoCartao, titular, limite, emitidoEm);
    }
}
