package br.com.zup.edu.propostas.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc(printOnlyOnFailure = false)
@ActiveProfiles("test")
public class BaseIntegrationTest {
    @Autowired
    protected ObjectMapper mapper;
    @Autowired
    protected MockMvc mockMvc;


    protected Long getIdLocation(String location) {
        int ultimaAparicaoDaBarra = location.lastIndexOf("/") + 1;

        return Long.valueOf(location.substring(ultimaAparicaoDaBarra));
    }
}
