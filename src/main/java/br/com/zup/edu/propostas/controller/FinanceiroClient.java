package br.com.zup.edu.propostas.controller;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
    name = "financeiroClient",
    url = "http://localhost:9999"
)
public interface FinanceiroClient {

    @PostMapping("/api/solicitacao")
    SubmeteParaAnaliseResponse submeteParaAnalise(@RequestBody SubmeteParaAnaliseRequest submeteParaAnaliseRequest);

}
