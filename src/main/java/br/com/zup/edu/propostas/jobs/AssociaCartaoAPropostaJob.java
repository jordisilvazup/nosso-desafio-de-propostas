package br.com.zup.edu.propostas.jobs;

import br.com.zup.edu.propostas.model.Proposta;
import br.com.zup.edu.propostas.repository.PropostaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;

import static br.com.zup.edu.propostas.controller.StatusDaProposta.*;

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

//    @Transactional // Contexto de Persistencia = EntityManager (Session) -> Cache de 1o Nivel -> Map<Long, Entity>
    @Scheduled(fixedDelay = 30 * 1000) // 30s
    public void executa() {
        boolean pendente = true;
        while (pendente) {

            pendente = transactionTemplate.execute((status) -> {

                List<Proposta> elegiveis = repository.findTop2ByStatusOrderByCriadaEmAsc(ELEGIVEL);
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
                    cartaoRepository.save(cartao); // INSERT

                    proposta.associaAo(cartao);
                    repository.save(proposta); // UPDATE
                });

                return true;
            });
        }
    } // commit

    private CartaoGeradoResponse buscaCartaPara(Proposta proposta) {
        try {
            CartaoGeradoResponse cartaoGerado = cartaoClient.buscaPorPropostaId(proposta.getId());
            return cartaoGerado;
        } catch (Exception e) {
            return null; // 4xx ou 500 = cartao ainda nao pronto ou nao encontrado
        }
    }
}
