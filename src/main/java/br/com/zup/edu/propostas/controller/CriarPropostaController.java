package br.com.zup.edu.propostas.controller;

import br.com.zup.edu.propostas.controller.request.PropostaRequest;
import br.com.zup.edu.propostas.model.Proposta;
import br.com.zup.edu.propostas.repository.PropostaRepository;
import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;

@RestController
public class CriarPropostaController {

    private final PropostaRepository repository;
    private final FinanceiroClient financeiroClient;

    public CriarPropostaController(PropostaRepository repository, FinanceiroClient financeiroClient) {
        this.repository = repository;
        this.financeiroClient = financeiroClient;
    }

    @Transactional
    @PostMapping("/api/v1/propostas")
    public ResponseEntity<?> criar(
            @RequestBody @Valid PropostaRequest request, UriComponentsBuilder uriComponentsBuilder
    ) {

        Proposta proposta = request.toModel(repository);
        repository.save(proposta); // INSERT -> MANAGED

        // TODO: submeter proposta para analise
        try {
            // 2xx ou 404 (null)
            SubmeteParaAnaliseResponse resultado = financeiroClient
                    .submeteParaAnalise(new SubmeteParaAnaliseRequest(
                            proposta.getId(), proposta.getDocumento(), proposta.getNome())
                    );

            if (resultado.getResultadoSolicitacao().equals("SEM_RESTRICAO")) {
                proposta.setStatus(StatusDaProposta.ELEGIVEL);
            } else {
                // status que eu NAO conheço
                proposta.setStatus(StatusDaProposta.NAO_ELEGIVEL);
            }

        } catch (FeignException.UnprocessableEntity e) { // 422
            // erro 422 = COM_RESTRICAO
            proposta.setStatus(StatusDaProposta.NAO_ELEGIVEL);
        }


        URI location = uriComponentsBuilder.path("/api/v1/propostas/{id}")
                .buildAndExpand(proposta.getId())
                .toUri();

        return ResponseEntity.created(location).build();

    }
}
