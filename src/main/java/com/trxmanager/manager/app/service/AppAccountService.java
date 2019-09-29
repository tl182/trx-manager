package com.trxmanager.manager.app.service;

import com.google.inject.Inject;
import com.trxmanager.manager.app.dto.InputAccount;
import com.trxmanager.manager.app.exception.InvalidValueException;
import com.trxmanager.manager.domain.dao.AccountDao;
import com.trxmanager.manager.domain.vo.Account;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

import static com.trxmanager.manager.util.Functional.numericFieldGtOrEq;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class AppAccountService {

    private final AccountDao accountDao;

    public Account findById(Long id) {
        return accountDao.findById(id);
    }

    public Account create(InputAccount inputAccount) {
        validateInputAccount(inputAccount);
        Account account = accountDao.create(mapToAccount(inputAccount));
        log.info("Created new account {}", account);
        return account;
    }

    public Account update(Long id, InputAccount inputAccount) {
        validateInputAccount(inputAccount);
        Account account = accountDao.update(mapToAccount(id, inputAccount));
        log.info("Updated account {}", account);
        return account;
    }

    private void validateInputAccount(InputAccount inputAccount) {
        boolean valid = numericFieldGtOrEq(InputAccount::getBalance, BigDecimal.ZERO).test(inputAccount);
        if (!valid) {
            throw new InvalidValueException("Invalid value " + inputAccount.toString());
        }
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
