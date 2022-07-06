package br.com.zup.edu.propostas.jobs;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
    name = "cartaoClient",
    url = "${integracoes.cartoes.url}"
)
public interface CartaoClient {

    @GetMapping("/api/cartoes")
    public CartaoGeradoResponse buscaPorPropostaId(@RequestParam(name = "idProposta", required = true) Long propostaId);

}
