package com.trxmanager.manager.app.service;

import com.trxmanager.manager.app.dto.InputTransfer;
import com.trxmanager.manager.app.exception.InvalidValueException;
import com.trxmanager.manager.domain.dao.TransferDao;
import com.trxmanager.manager.domain.service.TransferService;
import com.trxmanager.manager.domain.vo.Transfer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class AppTransferServiceTest {

    private TransferDao transferDao;
    private TransferService transferService;
    private AppTransferService appTransferService;

    @BeforeEach
    public void setUp() {
        transferDao = mock(TransferDao.class);
        transferService = mock(TransferService.class);
        appTransferService = new AppTransferService(transferDao, transferService);
    }

    @Test
    void create_zeroAmount() {
        InputTransfer inputTransfer = InputTransfer.builder()
                .fromId(1L)
                .toId(2L)
                .amount(BigDecimal.ZERO)
                .build();

        when(transferDao.create(any(Transfer.class))).thenReturn(
                Transfer.builder()
                        .id(10L)
                        .fromId(inputTransfer.getFromId())
                        .toId(inputTransfer.getToId())
                        .amount(inputTransfer.getAmount())
                        .status(Transfer.Status.CREATED)
                        .build());

        assertThrows(InvalidValueException.class, () -> appTransferService.create(inputTransfer));
    }

    @Test
    void create_positiveAmount() {
        InputTransfer inputTransfer = InputTransfer.builder()
                .fromId(1L)
                .toId(2L)
                .amount(BigDecimal.ONE)
                .build();

        when(transferDao.create(any(Transfer.class))).thenReturn(
                Transfer.builder()
                        .id(10L)
                        .fromId(inputTransfer.getFromId())
                        .toId(inputTransfer.getToId())
                        .amount(inputTransfer.getAmount())
                        .status(Transfer.Status.CREATED)
                        .build());

        assertDoesNotThrow(() -> appTransferService.create(inputTransfer));
    }

    @Test
    void create_negativeAmount() {
        InputTransfer inputTransfer = InputTransfer.builder()
                .fromId(1L)
                .toId(2L)
                .amount(BigDecimal.ONE.negate())
                .build();

        when(transferDao.create(any(Transfer.class))).thenReturn(
                Transfer.builder()
                        .id(10L)
                        .fromId(inputTransfer.getFromId())
                        .toId(inputTransfer.getToId())
                        .amount(inputTransfer.getAmount())
                        .status(Transfer.Status.CREATED)
                        .build());

        assertThrows(InvalidValueException.class, () -> appTransferService.create(inputTransfer));
    }

    @Test
    void create_nullFromId() {
        InputTransfer inputTransfer = InputTransfer.builder()
                .fromId(null)
                .toId(2L)
                .amount(BigDecimal.ONE)
                .build();

        when(transferDao.create(any(Transfer.class))).thenReturn(
                Transfer.builder()
                        .id(10L)
                        .fromId(inputTransfer.getFromId())
                        .toId(inputTransfer.getToId())
                        .amount(inputTransfer.getAmount())
                        .status(Transfer.Status.CREATED)
                        .build());

        assertThrows(InvalidValueException.class, () -> appTransferService.create(inputTransfer));
    }

    @Test
    void create_nullToId() {
        InputTransfer inputTransfer = InputTransfer.builder()
                .fromId(1L)
                .toId(null)
                .amount(BigDecimal.ONE)
                .build();

        when(transferDao.create(any(Transfer.class))).thenReturn(
                Transfer.builder()
                        .id(10L)
                        .fromId(inputTransfer.getFromId())
                        .toId(inputTransfer.getToId())
                        .amount(inputTransfer.getAmount())
                        .status(Transfer.Status.CREATED)
                        .build());

        assertThrows(InvalidValueException.class, () -> appTransferService.create(inputTransfer));
    }
}