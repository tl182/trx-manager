package com.trxmanager.manager.app.service;

import com.google.inject.Inject;
import com.trxmanager.manager.app.dto.InputTransfer;
import com.trxmanager.manager.domain.dao.TransferDao;
import com.trxmanager.manager.domain.service.TransferService;
import com.trxmanager.manager.domain.vo.Transfer;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.function.Predicate;

import static com.trxmanager.manager.util.Functional.fieldNonNull;
import static com.trxmanager.manager.util.Functional.numericFieldGt;

@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class AppTransferService {

    private final TransferDao transferDao;
    private final TransferService transferService;

    public Optional<Transfer> findById(Long id) {
        return transferDao.findById(id);
    }

    public Optional<Transfer> create(InputTransfer inputTransfer) {
        return Optional.ofNullable(inputTransfer)
                .filter(transferFilter())
                .map(this::mapToTransfer)
                .flatMap(transferDao::create);
    }

    private Predicate<InputTransfer> transferFilter() {
        return fieldNonNull(InputTransfer::getFromId)
                .and(fieldNonNull(InputTransfer::getToId))
                .and(numericFieldGt(InputTransfer::getAmount, BigDecimal.ZERO));
    }

    private Transfer mapToTransfer(InputTransfer inputTransfer) {
        return Transfer.builder()
                .fromId(inputTransfer.getFromId())
                .toId(inputTransfer.getToId())
                .amount(inputTransfer.getAmount())
                .build();
    }
}
