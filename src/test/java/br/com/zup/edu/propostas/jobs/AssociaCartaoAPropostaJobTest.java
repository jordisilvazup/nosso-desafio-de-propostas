package br.com.zup.edu.propostas.jobs;

import br.com.zup.edu.propostas.controller.StatusDaProposta;
import br.com.zup.edu.propostas.model.Proposta;
import br.com.zup.edu.propostas.repository.PropostaRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.Extensions;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.annotation.Timed;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static br.com.zup.edu.propostas.controller.StatusDaProposta.*;
import static java.util.concurrent.TimeUnit.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
class AssociaCartaoAPropostaJobTest {

    @Autowired
    private AssociaCartaoAPropostaJob job;

    @Autowired
    private PropostaRepository propostaRepository;

    @Autowired
    private CartaoRepository cartaoRepository;

    @MockBean
    private CartaoClient cartaoClient;

    @Autowired
    private EntityManager manager;

    /**
     * 1. @AfterEach: sujou, lavou;
     * 2. @BeforeEach: lavou, usou;
     * 3. @Rollback: usar louças descartavel
     */

    @BeforeEach
    public void setUp() {
        propostaRepository.deleteAll();
        cartaoRepository.deleteAll();
    }

    @Test
//    @Timed(millis = 2000)
//    @Timeout(value = 1, unit = SECONDS)
    @DisplayName("deve associar todas as propostas elegiveis aos seus cartoes")
    public void t1() {
        // cenario
        List.of(
            new Proposta("58622978044", "end.1", "p1", "p1@zup.com.br", BigDecimal.TEN),
            new Proposta("95706055025", "end.2", "p2", "p2@zup.com.br", BigDecimal.TEN),
            new Proposta("23096541086", "end.3", "p3", "p3@zup.com.br", BigDecimal.TEN),
            new Proposta("51022769057", "end.4", "p4", "p4@zup.com.br", BigDecimal.TEN),
            new Proposta("61499306024", "end.5", "p5", "p5@zup.com.br", BigDecimal.TEN)
        ).forEach(proposta -> {
            proposta.setStatus(ELEGIVEL);
            propostaRepository.save(proposta);
        });

        List.of(
            new Proposta("96534767007", "end.7", "p7", "p7@zup.com.br", BigDecimal.TEN)
        ).forEach(proposta -> {
            proposta.setStatus(NAO_ELEGIVEL);
            propostaRepository.save(proposta);
        });

        List.of(
            new Proposta("61135636001", "end.6", "p6", "p6@zup.com.br", BigDecimal.TEN)
        ).forEach(proposta -> {
            Cartao cartao = new Cartao("37903908490389033", "jordi", BigDecimal.TEN, LocalDateTime.now());
            cartaoRepository.save(cartao);

            proposta.associaAo(cartao);
            propostaRepository.save(proposta);
        });

        // mockar
        when(cartaoClient.buscaPorPropostaId(any())).then(invocationOnMock -> {
            CartaoGeradoResponse response = new CartaoGeradoResponse(
                    "cartao-" + UUID.randomUUID(), // numero do cartao UNICO
                    "titular",
                    LocalDateTime.now(),
                    BigDecimal.TEN,
                    "-1"
            );
            return response;
        });

        // ação
        job.executa();

        // validação
        assertEquals(0, countPropostasByStatus(ELEGIVEL), "total de propostas elegiveis");
        assertEquals(1, countPropostasByStatus(NAO_ELEGIVEL), "total de propostas NAO elegiveis");
        assertEquals(6, countPropostasByStatus(ELEGIVEL_COM_CARTAO_ASSOCIADO), "total de propostas elegiveis com cartoes associados");
        assertEquals(6, cartaoRepository.count(), "total de cartoes criados");
    }

    private int countPropostasByStatus(StatusDaProposta status) {
        return manager
                .createQuery("select count(p) from Proposta p where p.status = :status", Long.class)
                .setParameter("status", status)
                .getSingleResult().intValue()
            ;
    }
}