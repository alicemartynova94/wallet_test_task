package com.wallet.wallet_test_task;

import com.wallet.wallet_test_task.dao.WalletRepository;
import com.wallet.wallet_test_task.dto.WalletDto;
import com.wallet.wallet_test_task.dto.OperationType;
import com.wallet.wallet_test_task.entity.Wallet;
import com.wallet.wallet_test_task.exception_handling.IncorrectAmountException;
import com.wallet.wallet_test_task.exception_handling.InsufficientFundsException;
import com.wallet.wallet_test_task.exception_handling.InvalidOperationArgumentException;
import com.wallet.wallet_test_task.exception_handling.NoSuchWalletException;
import com.wallet.wallet_test_task.exception_handling.WalletDeletedException;
import com.wallet.wallet_test_task.service.WalletServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class WalletServiceImplTest {
    @Mock
    private WalletRepository walletRepository;
    @InjectMocks
    private WalletServiceImpl walletService;

    private Wallet wallet;
    private UUID walletId;
    private WalletDto walletDto;


    @BeforeEach
    void init() {
        wallet = new Wallet();
        walletId = UUID.randomUUID();
        walletDto = new WalletDto();

        wallet.setId(walletId);
        wallet.setAvailableFunds(5000.00);
        wallet.setWalletDeletedTime(null);

        walletDto.setId(wallet.getId());
        walletDto.setAmount(1000.00);
        walletDto.setOperationType(OperationType.WITHDRAW);
    }

    @Test
    public void updateWalletByDepositingOrWithdrawing_ExpectCorrectOperation() {
        when(walletRepository.findByIdActiveWallet(walletDto.getId())).thenReturn(Optional.of(wallet));

        walletService.updateWalletByDepositingOrWithdrawing(walletDto);

        Assertions.assertEquals(4000.00, wallet.getAvailableFunds());
    }

    @Test
    public void updateWalletByDepositingOrWithdrawing_ExpectIncorrectAmountException() {
        walletDto.setAmount(-1000.00);

        when(walletRepository.findByIdActiveWallet(walletDto.getId())).thenReturn(Optional.of(wallet));

        Assertions.assertThrows(IncorrectAmountException.class, () ->
                walletService.updateWalletByDepositingOrWithdrawing(walletDto));
    }

    @Test
    public void updateWalletByDepositingOrWithdrawing_ExpectInsufficientFundsException() {
        walletDto.setAmount(Double.MAX_VALUE);

        when(walletRepository.findByIdActiveWallet(walletDto.getId())).thenReturn(Optional.of(wallet));

        Assertions.assertThrows(InsufficientFundsException.class, () ->
                walletService.updateWalletByDepositingOrWithdrawing(walletDto));
    }

    @Test
    public void updateWalletByDepositingOrWithdrawing_ExpectInvalidOperationArgumentException() {
        walletDto.setOperationType(null);

        when(walletRepository.findByIdActiveWallet(walletDto.getId())).thenReturn(Optional.of(wallet));

        Assertions.assertThrows(InvalidOperationArgumentException.class, () ->
                walletService.updateWalletByDepositingOrWithdrawing(walletDto));
    }

    @Test
    public void updateWalletByDepositingOrWithdrawing_ExpectNoSuchWalletException() {
        when(walletRepository.findByIdActiveWallet(walletDto.getId())).thenReturn(Optional.empty());

        Assertions.assertThrows(NoSuchWalletException.class, () ->
                walletService.updateWalletByDepositingOrWithdrawing(walletDto));
    }

    @Test
    public void getWalletBalance_ExpectCorrectValueReturned() {
        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));

        Double availableFunds = walletService.getWalletBalance(walletId);

        Assertions.assertEquals(5000.00, availableFunds);
    }

    @Test
    public void getWalletBalance_ExpectWalletWasDeletedException() {
        wallet.setWalletDeletedTime(LocalDateTime.now());

        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));

        Assertions.assertThrows(WalletDeletedException.class, () ->
                walletService.getWalletBalance(walletId));
    }

    @Test
    public void getWalletBalance_ExpectNoSuchWalletException() {
        when(walletRepository.findById(walletId)).thenReturn(Optional.empty());

        Assertions.assertThrows(NoSuchWalletException.class, () ->
                walletService.getWalletBalance(walletId));
    }

}
