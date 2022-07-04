package br.com.zup.edu.propostas.controller;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
    name = "financeiroClient",
    url = "${integracoes.financeiro.url}"
)
public interface FinanceiroClient {

    @PostMapping("/api/solicitacao")
    SubmeteParaAnaliseResponse submeteParaAnalise(@RequestBody SubmeteParaAnaliseRequest submeteParaAnaliseRequest);

}
