package com.wallet.wallet_test_task;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wallet.wallet_test_task.dto.WalletDto;
import com.wallet.wallet_test_task.dto.OperationType;
import com.wallet.wallet_test_task.exception_handling.IncorrectAmountException;
import com.wallet.wallet_test_task.exception_handling.InsufficientFundsException;
import com.wallet.wallet_test_task.exception_handling.InvalidOperationArgumentException;
import com.wallet.wallet_test_task.exception_handling.NoSuchWalletException;
import com.wallet.wallet_test_task.service.WalletService;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.mockito.Mockito;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.UUID;

@WebMvcTest
public class WalletControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private WalletService walletService;
    @Autowired
    ObjectMapper objectMapper;

    UUID randomUUID;
    WalletDto walletDto;

    @BeforeEach
    void init() {
        randomUUID = UUID.randomUUID();
    }


    @Test
    public void updateWallet_ValidDtoData_ExpectStatus200() throws Exception {
        walletDto = new WalletDto(randomUUID, 500.00, OperationType.DEPOSIT);

        ResultActions resultActions = mockMvc.perform(post("/api/v1/wallet")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(walletDto)));

        resultActions.andExpect(status().isOk());
    }

    @Test
    public void updateWallet_IncorrectAmount_ExpectStatus400() throws Exception {
        walletDto = new WalletDto(randomUUID, -500.00, OperationType.DEPOSIT);

        doThrow(new IncorrectAmountException("The amount can not be null or a negative number"))
                .when(walletService).updateWalletByDepositingOrWithdrawing(Mockito.any(WalletDto.class));


        ResultActions resultActions = mockMvc.perform(post("/api/v1/wallet")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(walletDto)));

        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    public void updateWallet_InvalidOperationArgument_ExpectStatus400() throws Exception {
        walletDto = new WalletDto(randomUUID, 500.00, null);

        doThrow(new InvalidOperationArgumentException("Invalid operation type. Valid operation types: DEPOSIT, WITHDRAW"))
                .when(walletService).updateWalletByDepositingOrWithdrawing(Mockito.any(WalletDto.class));


        ResultActions resultActions = mockMvc.perform(post("/api/v1/wallet")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(walletDto)));

        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    public void updateWallet_InsufficientFunds_ExpectStatus409() throws Exception {
        walletDto = new WalletDto(randomUUID, Double.MAX_VALUE, OperationType.WITHDRAW);

        doThrow(new InsufficientFundsException("You are trying to withdraw more money than is available."))
                .when(walletService).updateWalletByDepositingOrWithdrawing(Mockito.any(WalletDto.class));


        ResultActions resultActions = mockMvc.perform(post("/api/v1/wallet")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(walletDto)));

        resultActions.andExpect(status().isConflict());
    }


    @Test
    public void getWalletBalance_ExpectStatus200() throws Exception {
        when(walletService.getWalletBalance(randomUUID)).thenReturn(5000.00);

        mockMvc.perform(get("/api/v1/wallets/{walletId}", randomUUID))
                .andExpect(status().isOk())
                .andExpect(content().string("5000.0"));
    }

    @Test
    public void getWalletBalance_ExpectStatus404() throws Exception {
        when(walletService.getWalletBalance(randomUUID)).thenThrow(new NoSuchWalletException("There is no wallet with such id."));

        mockMvc.perform(get("/api/v1/wallets/{walletId}", randomUUID))
                .andExpect(status().isNotFound())
                .andExpect(content().string("There is no wallet with such id."));
    }

}
