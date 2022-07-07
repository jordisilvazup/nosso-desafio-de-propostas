package br.com.zup.edu.propostas.controller;

import br.com.zup.edu.propostas.controller.request.PropostaRequest;
import br.com.zup.edu.propostas.model.Proposta;
import br.com.zup.edu.propostas.repository.PropostaRepository;
import feign.FeignException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;
import java.util.concurrent.Executor;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RestController
public class CriarPropostaController {

    private final PropostaRepository repository;
    private final FinanceiroClient financeiroClient;

    public CriarPropostaController(PropostaRepository repository, FinanceiroClient financeiroClient) {
        this.repository = repository;
        this.financeiroClient = financeiroClient;
    }

    /**
     * Processos 1-N Threads
     * Thread Java - Thread OS
     * <p>
     * Thread -> Connnection -> Thread ou Processo
     * 200 threads -> 200 connections * 2 = 400 connections
     * <p>
     * Pool de conexoes: 10 conexoes
     * - initial: 5
     * - max    : 30
     * - min    : 10
     * <p>
     * Request -> Thread (1-4mb)
     * Por padrão
     * - Tomcat: pool de 200 threads
     * - HikariCP - Pool de conexões
     */
    @Transactional // tem um Contexto de Persistencia
    @PostMapping("/api/v1/propostas")
    public ResponseEntity<?> criar(
            @RequestBody @Valid PropostaRequest request, UriComponentsBuilder uriComponentsBuilder
    ) {

        Proposta proposta = request.toModel(repository);
        repository.save(proposta); // INSERT -> MANAGED

        StatusDaProposta status = submetePropostaParaAnalise(proposta);
        proposta.setStatus(status);

        URI location = uriComponentsBuilder.path("/api/v1/propostas/{id}")
                .buildAndExpand(proposta.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    private StatusDaProposta submetePropostaParaAnalise(Proposta proposta) {
        // timeout
        // retry
        // retry + backoff; 1, 3, 5, 7, ....
        // retry + exponential backoff; 1s, 2s, 4, 8, 16, 32...
        // retry + exponential backoff + jitter; 1.004, 2.123, 4.045, 7.987, 16, 32...
        // circuit breaker
        // bulkhead
        // ...
        try {
            // 2xx ou 404 (null) - IDEMpotente
            SubmeteParaAnaliseResponse resultado = financeiroClient
                    .submeteParaAnalise(new SubmeteParaAnaliseRequest(
                            proposta.getId(), proposta.getDocumento(), proposta.getNome())
                    );

            // early return
            return resultado.toStatusDaProposta();

        } catch (FeignException.UnprocessableEntity e) {   // erro 422 = COM_RESTRICAO
            return StatusDaProposta.NAO_ELEGIVEL;
        } catch (Exception e) {
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR,
                    "não foi possivel submeter proposta para analise");
        }
    }
}
