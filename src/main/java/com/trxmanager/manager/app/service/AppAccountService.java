package com.trxmanager.manager.app.service;

import com.trxmanager.manager.app.dto.AppAccount;

import java.math.BigDecimal;
import java.util.Optional;

public class AppAccountService {

    public Optional<AppAccount> findById(Long id) {
        BigDecimal balance = new BigDecimal("1234.567");
        AppAccount account = AppAccount.builder()
                .id(1L)
                .balance(balance)
                .build();
        return Optional.of(account);
    }

    public AppAccount create(AppAccount newAccount) {
        return AppAccount.builder()
                .id(2L)
                .balance(newAccount.getBalance())
                .build();
    }
}
