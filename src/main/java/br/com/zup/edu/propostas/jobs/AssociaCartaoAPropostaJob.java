package br.com.zup.edu.propostas.jobs;

import br.com.zup.edu.propostas.controller.StatusDaProposta;
import br.com.zup.edu.propostas.model.Proposta;
import br.com.zup.edu.propostas.repository.PropostaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class AssociaCartaoAPropostaJob {

    @Autowired
    private PropostaRepository repository;
    @Autowired
    private CartaoClient cartaoClient;
    @Autowired
    private CartaoRepository cartaoRepository;


    @Transactional
    @Scheduled(fixedDelay = 5 * 1000)
    public void executa() {

        // buscar as propostas elegiveis
        List<Proposta> elegiveis = repository.findAllByStatusAndCartaoIsNullOrderByCriadaEmAsc(StatusDaProposta.ELEGIVEL);
        elegiveis.forEach(proposta -> {

            CartaoGeradoResponse cartaoGerado = cartaoClient.buscaPorPropostaId(proposta.getId());

            Cartao cartao = cartaoGerado.toModel();
            cartaoRepository.save(cartao);

            proposta.associaAo(cartao);
            repository.save(proposta);
        });


        // para cada proposta
        //  verificar se existe cartao no sistema externo
        //      se encontrar cartão, entao associa a proposta
        //      caso contrario, ignora e deixa para tentar mais tarde

    }
}
