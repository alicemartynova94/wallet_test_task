package com.wallet.wallet_test_task.mappers;

import com.wallet.wallet_test_task.dto.WalletDto;
import com.wallet.wallet_test_task.entity.Wallet;
import org.mapstruct.Mapper;

@Mapper
public interface WalletMapper {

    Wallet walletDtoToWallet(WalletDto walletDto);

    WalletDto walletToWalletDto(Wallet wallet);
}
