package com.wallet.wallet_test_task.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WalletDto {
    private UUID id;
    private Double amount;
    private OperationType operationType;
}
