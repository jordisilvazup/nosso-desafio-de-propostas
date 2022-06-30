package br.com.zup.edu.propostas.controller;

import br.com.zup.edu.propostas.controller.request.PropostaRequest;
import br.com.zup.edu.propostas.model.Proposta;
import br.com.zup.edu.propostas.repository.PropostaRepository;
import br.com.zup.edu.propostas.utils.BaseIntegrationTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class CriarPropostaControllerTest extends BaseIntegrationTest {
    @Autowired
    private PropostaRepository repository;


    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

    @Test
    @DisplayName("deve criar uma proposta para cartao")
    void test() throws Exception {
        PropostaRequest propostaRequest = new PropostaRequest(
                "65.804.581/0001-60",
                "Rua das Gamelheiras n 82 , Barro Branco, Rio de Janeiro",
                "Jordi Henrique Marques Silva",
                "jordi.silva@zup.com.br",
                new BigDecimal("2500.00")
        );

        String payload = mapper.writeValueAsString(propostaRequest);

        String location = mockMvc.perform(
                        post("/api/v1/propostas")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(payload)
                )
                .andExpect(status().isCreated())
                .andExpect(redirectedUrlPattern("http://localhost/api/v1/propostas/*"))
                .andReturn()
                .getResponse()
                .getHeader("location");

        assertNotNull(location);

        Long id = getIdLocation(location);

        assertTrue(
                repository.existsById(id),
                "deveria exisitr uma proposta para o id informado"
        );
    }

    @Test
    @DisplayName("nao deve criar uma proposta caso os dados estejam invalidos")
    void test1() throws Exception {
        PropostaRequest propostaRequest = new PropostaRequest(
                null,
                null,
                null,
                null,
                null
        );

        String payloadRequest = mapper.writeValueAsString(propostaRequest);

        String payloadResponse = mockMvc.perform(
                        post("/api/v1/propostas")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(payloadRequest)
                                .header("Accept-Language", "pt-br")
                )
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        TypeFactory typeFactory = mapper.getTypeFactory();

        List<String> response = mapper.readValue(
                payloadResponse,
                typeFactory.constructCollectionType(List.class, String.class)
        );

        assertThat(response)
                .isNotNull()
                .hasSize(5)
                .contains(
                        "O campo email não deve estar em branco",
                        "O campo salario não deve ser nulo",
                        "O campo endereco não deve estar em branco",
                        "O campo documento não deve estar em branco",
                        "O campo nome não deve estar em branco"
                );
    }

    @Test
    @DisplayName("nao deve criar uma proposta caso o documento nao seja valido")
    void test2() throws Exception {
        PropostaRequest propostaRequest = new PropostaRequest(
                "65.804.581/0121-60",
                "Rua das Gamelheiras n 82 , Barro Branco, Rio de Janeiro",
                "Jordi Henrique Marques Silva",
                "jordi.silva@zup.com.br",
                new BigDecimal("2500.00")
        );

        String payloadRequest = mapper.writeValueAsString(propostaRequest);

        String payloadResponse = mockMvc.perform(
                        post("/api/v1/propostas")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(payloadRequest)
                                .header("Accept-Language", "pt-br")
                )
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        TypeFactory typeFactory = mapper.getTypeFactory();

        List<String> response = mapper.readValue(
                payloadResponse,
                typeFactory.constructCollectionType(List.class, String.class)
        );

        assertThat(response)
                .isNotNull()
                .hasSize(1)
                .contains(
                        "O campo documento deve estar em formato e valor coerente a especificação da Receita Federal Brasileira"
                );
    }

    @Test
    @DisplayName("nao deve criar uma proposta caso o email nao seja valido")
    void test3() throws Exception {
        PropostaRequest propostaRequest = new PropostaRequest(
                "65.804.581/0001-60",
                "Rua das Gamelheiras n 82 , Barro Branco, Rio de Janeiro",
                "Jordi Henrique Marques Silva",
                "jordi.silvazup.com.br",
                new BigDecimal("2500.00")
        );

        String payloadRequest = mapper.writeValueAsString(propostaRequest);

        String payloadResponse = mockMvc.perform(
                        post("/api/v1/propostas")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(payloadRequest)
                                .header("Accept-Language", "pt-br")
                )
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        TypeFactory typeFactory = mapper.getTypeFactory();

        List<String> response = mapper.readValue(
                payloadResponse,
                typeFactory.constructCollectionType(List.class, String.class)
        );

        assertThat(response)
                .isNotNull()
                .hasSize(1)
                .contains(
                        "O campo email deve ser um endereço de e-mail bem formado"
                );
    }

    @Test
    @DisplayName("nao deve criar uma proposta caso o salario sejam menor igual a zero")
    void test4() throws Exception {
        PropostaRequest propostaRequest = new PropostaRequest(
                "65.804.581/0001-60",
                "Rua das Gamelheiras n 82 , Barro Branco, Rio de Janeiro",
                "Jordi Henrique Marques Silva",
                "jordi.silva@zup.com.br",
                BigDecimal.ZERO
        );

        String payloadRequest = mapper.writeValueAsString(propostaRequest);

        String payloadResponse = mockMvc.perform(
                        post("/api/v1/propostas")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(payloadRequest)
                                .header("Accept-Language", "pt-br")
                )
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        TypeFactory typeFactory = mapper.getTypeFactory();

        List<String> response = mapper.readValue(
                payloadResponse,
                typeFactory.constructCollectionType(List.class, String.class)
        );

        assertThat(response)
                .isNotNull()
                .hasSize(1)
                .contains(
                        "O campo salario deve ser maior que 0"
                );
    }
}