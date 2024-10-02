package com.wallet.wallet_test_task.service;

import com.wallet.wallet_test_task.dao.WalletRepository;
import com.wallet.wallet_test_task.dto.WalletDto;
import com.wallet.wallet_test_task.entity.Wallet;
import com.wallet.wallet_test_task.exception_handling.IncorrectAmountException;
import com.wallet.wallet_test_task.exception_handling.InsufficientFundsException;
import com.wallet.wallet_test_task.exception_handling.InvalidOperationArgumentException;
import com.wallet.wallet_test_task.exception_handling.NoSuchWalletException;
import com.wallet.wallet_test_task.exception_handling.WalletDeletedException;
import com.wallet.wallet_test_task.mappers.WalletMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Slf4j
public class WalletServiceImpl implements WalletService {
    @Autowired
    private WalletRepository walletRepository;
    @Autowired
    private WalletMapper walletMapper;

    @Override
    public void updateWalletByDepositingOrWithdrawing(WalletDto walletDto) {
        log.debug("Fetching wallet with id: {}", walletDto.getId());
        Wallet wallet = walletRepository.findByIdActiveWalletLock(walletDto.getId()).orElseThrow(() -> new NoSuchWalletException("There is no wallet with " + walletDto.getId() + "in DB or it was deleted."));

        if (walletDto.getAmount() == null || walletDto.getAmount() <= 0) {
            throw new IncorrectAmountException("The amount can not be null or a negative number");
        }

        double availableFunds = wallet.getAvailableFunds();
        switch (walletDto.getOperationType()) {
            case DEPOSIT:
                wallet.setAvailableFunds(availableFunds + walletDto.getAmount());
                break;
            case WITHDRAW:
                if (availableFunds < walletDto.getAmount()) {
                    throw new InsufficientFundsException("You are trying to withdraw more money than is available. Available sum is " + availableFunds);
                }
                wallet.setAvailableFunds(availableFunds - walletDto.getAmount());
                break;
            case null:
                throw new InvalidOperationArgumentException("Invalid operation type. Valid operation types: DEPOSIT, WITHDRAW");
            default:
                throw new InvalidOperationArgumentException("Invalid operation type. Valid operation types: DEPOSIT, WITHDRAW");
        }
        log.debug("Saving updated funds for the wallet with id: {}", walletDto.getId());
        walletRepository.save(wallet);
    }

    @Override
    public Double getWalletBalance(UUID walletId) {
        log.debug("Fetching wallet with id: {}", walletId);
        Wallet wallet = walletRepository
                .findById(walletId)
                .orElseThrow(() -> new NoSuchWalletException("There is no wallet with " + walletId + "in DB."));

        if (wallet.getWalletDeletedTime() != null) {
            throw new WalletDeletedException("Wallet with id " + walletId + " was deleted and cannot be accessed.");
        }

        log.debug("Getting available funds for the wallet with id: {}", walletId);
        return wallet.getAvailableFunds();
    }

    @Override
    public void softDelete(UUID walletId) {
        log.debug("Fetching wallet with id: {}", walletId);
        Wallet wallet = walletRepository
                .findById(walletId)
                .orElseThrow(() -> new NoSuchWalletException("There is no wallet with " + walletId + "in DB."));

        wallet.setWalletDeletedTime(LocalDateTime.now());

        walletRepository.save(wallet);
    }
}
