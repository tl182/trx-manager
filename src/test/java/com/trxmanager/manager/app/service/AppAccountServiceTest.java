package com.trxmanager.manager.app.service;

import com.trxmanager.manager.app.dto.InputAccount;
import com.trxmanager.manager.domain.dao.AccountDao;
import com.trxmanager.manager.domain.vo.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class AppAccountServiceTest {

    private AccountDao accountDao;
    private AppAccountService appAccountService;

    @BeforeEach
    public void setUp() {
        accountDao = mock(AccountDao.class);
        appAccountService = new AppAccountService(accountDao);
    }

    @Test
    void create_zeroBalance() {
        InputAccount inputAccount = InputAccount.builder().balance(BigDecimal.ZERO).build();

        when(accountDao.create(any(Account.class))).thenReturn(Account.builder().build());

        Optional<Account> account = appAccountService.create(inputAccount);
        assertTrue(account.isPresent());
    }

    @Test
    void create_positiveBalance() {
        InputAccount inputAccount = InputAccount.builder().balance(BigDecimal.ONE).build();

        when(accountDao.create(any(Account.class))).thenReturn(Account.builder().build());

        Optional<Account> account = appAccountService.create(inputAccount);
        assertTrue(account.isPresent());
    }

    @Test
    void create_negativeBalance() {
        InputAccount inputAccount = InputAccount.builder().balance(BigDecimal.ONE.negate()).build();

        when(accountDao.create(any(Account.class))).thenReturn(Account.builder().build());

        Optional<Account> account = appAccountService.create(inputAccount);
        assertFalse(account.isPresent());
    }

    @Test
    void create_nullBalance() {
        InputAccount inputAccount = InputAccount.builder().balance(null).build();

        when(accountDao.create(any(Account.class))).thenReturn(Account.builder().build());

        Optional<Account> account = appAccountService.create(inputAccount);
        assertFalse(account.isPresent());
    }

    @Test
    void update_zeroBalance() {
        Long id = 1L;
        InputAccount inputAccount = InputAccount.builder().balance(BigDecimal.ZERO).build();

        when(accountDao.update(any(Account.class))).thenReturn(Optional.of(Account.builder().build()));

        Optional<Account> account = appAccountService.update(id, inputAccount);
        assertTrue(account.isPresent());
    }

    @Test
    void update_positiveBalance() {
        Long id = 1L;
        InputAccount inputAccount = InputAccount.builder().balance(BigDecimal.ONE).build();

        when(accountDao.update(any(Account.class))).thenReturn(Optional.of(Account.builder().build()));

        Optional<Account> account = appAccountService.update(id, inputAccount);
        assertTrue(account.isPresent());
    }

    @Test
    void update_negativeBalance() {
        Long id = 1L;
        InputAccount inputAccount = InputAccount.builder().balance(BigDecimal.ONE.negate()).build();

        when(accountDao.update(any(Account.class))).thenReturn(Optional.of(Account.builder().build()));

        Optional<Account> account = appAccountService.update(id, inputAccount);
        assertFalse(account.isPresent());
    }

    @Test
    void update_nullBalance() {
        Long id = 1L;
        InputAccount inputAccount = InputAccount.builder().balance(null).build();

        when(accountDao.update(any(Account.class))).thenReturn(Optional.of(Account.builder().build()));

        Optional<Account> account = appAccountService.update(id, inputAccount);
        assertFalse(account.isPresent());
    }
}