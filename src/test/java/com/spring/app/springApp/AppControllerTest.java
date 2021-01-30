package com.spring.app.springApp;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AppController.class)
class AppControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionRepository transactionRepository;

    private final Transactions TRANSACTION_1 = Transactions.builder()
            .id(1L)
            .accountNumber(12345)
            .amount(new BigDecimal("1.23"))
            .build();

    private final Transactions TRANSACTION_2 = Transactions.builder()
            .accountNumber(678910)
            .amount(new BigDecimal("33.40"))
            .build();

    @Test
    public void paymentsPostPaymentsEndpointReturns201Response2() throws Exception {
        //given
        when(transactionRepository.save(any(Transactions.class))).thenReturn(TRANSACTION_1);

        //when
        mockMvc.perform(post("/payments")
                .contentType("application/json")
                .content("{\"accountNumber\":12345, \"amount\": 1.23}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.accountNumber").value(12345))
                .andExpect(jsonPath("$.amount").value(1.23));

        //then
        verify(transactionRepository, times(1)).save(any(Transactions.class));
    }

    @Test
    public void paymentsUpdatePaymentEndpointReturns200Response() throws Exception {
        //given
        Transactions amendedTransaction1 = Transactions.builder()
                .id(1L)
                .accountNumber(12345)
                .amount(new BigDecimal("3.23"))
                .build();

        when(transactionRepository.findById(1L)).thenReturn(Optional.of(TRANSACTION_1));
        when(transactionRepository.save(any(Transactions.class))).thenReturn(amendedTransaction1);

        //when
        mockMvc.perform(put("/updatePayment/{id}", 1)
                .contentType("application/json")
                .content("{\"id\": 1, \"accountNumber\":12345, \"amount\": 3.23}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.accountNumber").value(12345))
                .andExpect(jsonPath("$.amount").value(3.23));

        //then
        verify(transactionRepository, times(1)).findById(1L);
        verify(transactionRepository, times(1)).save(any(Transactions.class));
    }

    @Test
    public void paymentsGetPaymentsEndpointReturns200() throws Exception {
        //given
        when(transactionRepository.findAll()).thenReturn(Arrays.asList(TRANSACTION_1, TRANSACTION_2));

        //when
        mockMvc.perform(get("/allPayments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].accountNumber").value(12345))
                .andExpect(jsonPath("$[0].amount").value(1.23))
                .andExpect(jsonPath("$[1].accountNumber").value(678910))
                .andExpect(jsonPath("$[1].amount").value(33.40));

        //then
        verify(transactionRepository, times(1)).findAll();
    }

    @Test
    public void paymentsGetPaymentByIdEndpointReturns200() throws Exception {
        //given
        when(transactionRepository.findById(1L)).thenReturn(Optional.of(TRANSACTION_1));

        //when
        mockMvc.perform(get("/payment/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountNumber").value(12345))
                .andExpect(jsonPath("$.amount").value(1.23));

        //then
        verify(transactionRepository, times(1)).findById(1L);
    }

    @Test
    public void paymentsDeleteAllPaymentsEndpointReturns200() throws Exception {
        //given
        transactionRepository.deleteAll(Arrays.asList(TRANSACTION_1, TRANSACTION_2));

        //when
        mockMvc.perform(delete("/deleteAllPayments"))
                .andExpect(status().isNoContent());

        //then
        verify(transactionRepository, times(1)).deleteAll(Arrays.asList(TRANSACTION_1, TRANSACTION_2));
    }

    @Test
    public void paymentsDeleteByIdEndpointReturns200() throws Exception {
        //given
        transactionRepository.delete(TRANSACTION_1);

        //when
        mockMvc.perform(delete("/deletePayment/{id}", 1))
                .andExpect(status().isNoContent());

        //then
        verify(transactionRepository, times(1)).deleteById(1L);
    }
}