package com.trxmanager.manager.app.service;

import com.google.inject.Inject;
import com.trxmanager.manager.app.dto.InputAccount;
import com.trxmanager.manager.domain.dao.AccountDao;
import com.trxmanager.manager.domain.vo.Account;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.Optional;

import static com.trxmanager.manager.util.Functional.numericFieldGtOrEq;

@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class AppAccountService {

    private final AccountDao accountDao;

    public Optional<Account> findById(Long id) {
        return accountDao.findById(id);
    }

    public Optional<Account> create(InputAccount inputAccount) {
        return Optional.ofNullable(inputAccount)
                .filter(numericFieldGtOrEq(InputAccount::getBalance, BigDecimal.ZERO))
                .map(this::mapToAccount)
                .map(accountDao::create);
    }

    public Optional<Account> update(Long id, InputAccount inputAccount) {
        return Optional.ofNullable(inputAccount)
                .filter(numericFieldGtOrEq(InputAccount::getBalance, BigDecimal.ZERO))
                .map(ic -> mapToAccount(id, ic))
                .flatMap(accountDao::update);
    }

    private Account mapToAccount(InputAccount inputAccount) {
        return Account.builder()
                .balance(inputAccount.getBalance())
                .build();
    }

    private Account mapToAccount(Long id, InputAccount inputAccount) {
        return Account.builder()
                .id(id)
                .balance(inputAccount.getBalance())
                .build();
    }
}
