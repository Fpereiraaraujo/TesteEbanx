package com.FernandoPereira.TesteEbanx.framework.adapter.in.controllers;

import com.FernandoPereira.TesteEbanx.application.port.in.AccountPortIn;
import com.FernandoPereira.TesteEbanx.domain.dto.AccountDTO;
import com.FernandoPereira.TesteEbanx.domain.dto.EventRequestDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccountController.class)
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountPortIn accountPortIn;

    @Autowired
    private ObjectMapper objectMapper;

    // --- TESTE DE RESET (Novo) ---
    @Test
    void shouldResetAndReturn200() throws Exception {
        mockMvc.perform(post("/reset"))
                .andExpect(status().isOk())
                .andExpect(content().string("OK"));


        verify(accountPortIn, times(1)).reset();
    }

    // --- TESTE DE SALDO ---
    @Test
    void shouldReturn404WhenBalanceNotFound() throws Exception {
        when(accountPortIn.getBalance("123")).thenReturn(null);

        mockMvc.perform(get("/balance")
                        .param("account_id", "123"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("0"));
    }

    // --- TESTE DE DEPÓSITO ---
    @Test
    void shouldDepositAndReturn201() throws Exception {
        EventRequestDTO request = new EventRequestDTO();
        request.setType("deposit");
        request.setDestination("100");
        request.setAmount(BigDecimal.TEN);

        AccountDTO responseDTO = new AccountDTO("100", BigDecimal.TEN);
        when(accountPortIn.deposit("100", BigDecimal.TEN)).thenReturn(responseDTO);

        mockMvc.perform(post("/event")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().json("{\"id\":\"100\", \"balance\":10}"));
    }

    @Test
    void shouldReturn200AndBalanceWhenAccountExists() throws Exception {

        AccountDTO account = new AccountDTO("100", BigDecimal.valueOf(20));
        when(accountPortIn.getBalance("100")).thenReturn(account);

        mockMvc.perform(get("/balance")
                        .param("account_id", "100"))
                .andExpect(status().isOk())
                .andExpect(content().json("20"));
    }





    // --- TESTE DE TRANSFERÊNCIA
    @Test
    void shouldTransferAndReturn201() throws Exception {
        // Cenário: Transferir 50 da conta "100" para a "300"
        EventRequestDTO request = new EventRequestDTO();
        request.setType("transfer");
        request.setOrigin("100");
        request.setDestination("300");
        request.setAmount(BigDecimal.valueOf(50));

        //conta de origem (100) fica com saldo 0
        AccountDTO originUpdated = new AccountDTO("100", BigDecimal.ZERO);
        //  conta de destino (300) recebe 50 e fica com 50
        AccountDTO destinationUpdated = new AccountDTO("300", BigDecimal.valueOf(50));


        when(accountPortIn.transfer("100", "300", BigDecimal.valueOf(50)))
                .thenReturn(originUpdated);


        when(accountPortIn.getBalance("300")).thenReturn(destinationUpdated);


        mockMvc.perform(post("/event")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().json("""
                    {
                        "origin": {"id":"100", "balance":0},
                        "destination": {"id":"300", "balance":50}
                    }
                """));
    }

    // --- TESTE DE SAQUE ---
    @Test
    void shouldReturn404WhenWithdrawFromNonExistentAccount() throws Exception {
        EventRequestDTO request = new EventRequestDTO();
        request.setType("withdraw");
        request.setOrigin("200");
        request.setAmount(BigDecimal.TEN);

        when(accountPortIn.withdraw("200", BigDecimal.TEN))
                .thenThrow(new RuntimeException("Account not found"));

        mockMvc.perform(post("/event")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("0"));
    }
}