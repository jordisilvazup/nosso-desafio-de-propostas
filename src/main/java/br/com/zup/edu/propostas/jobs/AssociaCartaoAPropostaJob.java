package br.com.zup.edu.propostas.jobs;

import br.com.zup.edu.propostas.model.Proposta;
import br.com.zup.edu.propostas.repository.PropostaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;

import static br.com.zup.edu.propostas.controller.StatusDaProposta.*;

@ConditionalOnExpression("${jobs.associaPropostas.ativo}")
@Service
public class AssociaCartaoAPropostaJob {

    private static final Logger LOGGER = LoggerFactory.getLogger(AssociaCartaoAPropostaJob.class);

    @Autowired
    private PropostaRepository repository;
    @Autowired
    private CartaoClient cartaoClient;
    @Autowired
    private CartaoRepository cartaoRepository;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Value("${jobs.associaPropostas.ativo}")
    private Boolean ativo;

//    @Transactional // Contexto de Persistencia = EntityManager (Session) -> Cache de 1o Nivel -> Map<Long, Entity>
    @Scheduled(fixedDelay = 30 * 1000, initialDelay = 5 * 1000) // 30s
    public void executa() {

        Boolean pendente = true;
        while (pendente) {
            pendente = transactionTemplate.execute((status) -> {

                List<Proposta> elegiveis = repository.findTop10ByStatusOrderByCriadaEmAsc(ELEGIVEL);
                if (elegiveis.isEmpty()) {
                    return false;
                }

                LOGGER.info("{}", elegiveis);
                elegiveis.forEach(proposta -> {

                    CartaoGeradoResponse cartaoGerado = buscaCartaPara(proposta);
                    if (cartaoGerado == null) {
                        return;
                    }

                    Cartao cartao = cartaoGerado.toModel();
                    cartaoRepository.save(cartao); // MANAGED -> ID -> nexval() | IDENTITY -> INSERT

                    proposta.associaAo(cartao);
                    repository.save(proposta); // UPDATE
                });

                return true;
            }); // commit
        }
    }

    private CartaoGeradoResponse buscaCartaPara(Proposta proposta) {
        try {
            CartaoGeradoResponse cartaoGerado = cartaoClient.buscaPorPropostaId(proposta.getId());
            return cartaoGerado;
        } catch (Exception e) {
            return null; // 4xx ou 500 = cartao ainda nao pronto ou nao encontrado
        }
    }
}
