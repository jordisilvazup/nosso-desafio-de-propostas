package br.com.zup.edu.propostas.controller;

import br.com.zup.edu.propostas.controller.request.PropostaRequest;
import br.com.zup.edu.propostas.model.Proposta;
import br.com.zup.edu.propostas.repository.PropostaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
public class CriarPropostaController {
    private final PropostaRepository repository;

    public CriarPropostaController(PropostaRepository repository) {
        this.repository = repository;
    }

    @PostMapping("/api/v1/propostas")
    public ResponseEntity<?> criar(
            @RequestBody @Valid PropostaRequest request, UriComponentsBuilder uriComponentsBuilder
    ) {

        Proposta proposta = request.toModel(repository);

        repository.save(proposta);

        URI location = uriComponentsBuilder.path("/api/v1/propostas/{id}")
                .buildAndExpand(proposta.getId())
                .toUri();

        return ResponseEntity.created(location).build();

    }
}
