package com.trxmanager.manager.app.service;

import com.trxmanager.manager.app.dto.InputAccount;
import com.trxmanager.manager.app.exception.InvalidValueException;
import com.trxmanager.manager.domain.dao.AccountDao;
import com.trxmanager.manager.domain.vo.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

        when(accountDao.create(any(Account.class))).thenReturn(Account.builder().id(1L).balance(inputAccount.getBalance()).build());

        assertDoesNotThrow(() -> appAccountService.create(inputAccount));
    }

    @Test
    void create_positiveBalance() {
        InputAccount inputAccount = InputAccount.builder().balance(BigDecimal.ONE).build();

        when(accountDao.create(any(Account.class))).thenReturn(Account.builder().id(1L).balance(inputAccount.getBalance()).build());

        assertDoesNotThrow(() -> appAccountService.create(inputAccount));
    }

    @Test
    void create_negativeBalance() {
        InputAccount inputAccount = InputAccount.builder().balance(BigDecimal.ONE.negate()).build();

        when(accountDao.create(any(Account.class))).thenReturn(Account.builder().id(1L).balance(inputAccount.getBalance()).build());

        assertThrows(InvalidValueException.class, () -> appAccountService.create(inputAccount));
    }

    @Test
    void create_nullBalance() {
        InputAccount inputAccount = InputAccount.builder().balance(null).build();

        when(accountDao.create(any(Account.class))).thenReturn(Account.builder().id(1L).balance(inputAccount.getBalance()).build());

        assertThrows(InvalidValueException.class, () -> appAccountService.create(inputAccount));
    }

    @Test
    void update_zeroBalance() {
        Long id = 1L;
        InputAccount inputAccount = InputAccount.builder().balance(BigDecimal.ZERO).build();

        when(accountDao.update(any(Account.class))).thenReturn(Account.builder().id(id).balance(inputAccount.getBalance()).build());

        assertDoesNotThrow(() -> appAccountService.update(id, inputAccount));
    }

    @Test
    void update_positiveBalance() {
        Long id = 1L;
        InputAccount inputAccount = InputAccount.builder().balance(BigDecimal.ONE).build();

        when(accountDao.update(any(Account.class))).thenReturn(Account.builder().id(id).balance(inputAccount.getBalance()).build());

        assertDoesNotThrow(() -> appAccountService.update(id, inputAccount));
    }

    @Test
    void update_negativeBalance() {
        Long id = 1L;
        InputAccount inputAccount = InputAccount.builder().balance(BigDecimal.ONE.negate()).build();

        when(accountDao.update(any(Account.class))).thenReturn(Account.builder().id(id).balance(inputAccount.getBalance()).build());

        assertThrows(InvalidValueException.class, () -> appAccountService.update(id, inputAccount));
    }

    @Test
    void update_nullBalance() {
        Long id = 1L;
        InputAccount inputAccount = InputAccount.builder().balance(null).build();

        when(accountDao.update(any(Account.class))).thenReturn(Account.builder().id(id).balance(inputAccount.getBalance()).build());

        assertThrows(InvalidValueException.class, () -> appAccountService.update(id, inputAccount));
    }
}