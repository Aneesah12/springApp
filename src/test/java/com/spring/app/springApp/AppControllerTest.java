package com.spring.app.springApp;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AppController.class)
class AppControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionRepository transactionRepository;

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void paymentsEndpointReturns201Response() throws Exception {
        MvcResult result = mockMvc.perform(post("/payments")
                .contentType("application/json")
                .content("{\"accountNumber\":12345, \"amount\": 1.23}"))
                .andExpect(status().isOk()).andReturn();

        String contentAsString = result.getResponse().getContentAsString();

//        assertEquals(1.23, contentAsString);
    }

    @Test
    public void paymentsEndpointReturns201Response2() throws Exception {
        //given
        Transactions expectedTransactions = Transactions.builder()
                .accountNumber(12345)
                .amount(new BigDecimal("1.23"))
                .build();

        when(transactionRepository.save(any(Transactions.class))).thenReturn(expectedTransactions);

        mockMvc.perform(post("/payments")
                .contentType("application/json")
                .content("{\"accountNumber\":12345, \"amount\": 1.23}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.accountNumber").value(12345))
                .andExpect(jsonPath("$.amount").value(1.23));

        verify(transactionRepository, times(1)).save(any(Transactions.class));
    }
}