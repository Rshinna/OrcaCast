package rshinna.orcacast.infrastructure.web;

import tools.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import rshinna.orcacast.application.ListTransactionByCategoryUseCase;
import rshinna.orcacast.application.PersistTransactionUseCase;
import rshinna.orcacast.application.output.TransactionOutput;
import rshinna.orcacast.domain.Category;
import rshinna.orcacast.infrastructure.web.dto.CreateTransactionRequest;

import java.math.BigDecimal;
import java.time.Instant;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TransactionController.class)
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PersistTransactionUseCase persistTransactionUseCase;

    @MockitoBean
    private ListTransactionByCategoryUseCase listTransactionByCategoryUseCase;

    @Test
    void deveCriarTransacaoComSucesso() throws Exception {
        var output = new TransactionOutput(
                "algum-id",
                "mercado",
                "GROCERIES",
                new BigDecimal("50.00"),
                Instant.now());
        when(persistTransactionUseCase.execute(any())).thenReturn(output);

        var request = new CreateTransactionRequest("mercado", 5000, Category.GROCERIES);

        mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.description")
                        .value("mercado"));
    }

    @Test
    void deveRejeitarTransacaoComValorNegativo() throws Exception {
        var request = new CreateTransactionRequest("teste inválido", -100, Category.GROCERIES);

        mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(persistTransactionUseCase);
    }
}