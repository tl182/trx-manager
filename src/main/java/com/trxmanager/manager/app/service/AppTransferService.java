package com.trxmanager.manager.app.service;

import com.google.inject.Inject;
import com.trxmanager.manager.app.dto.InputTransfer;
import com.trxmanager.manager.app.exception.InvalidValueException;
import com.trxmanager.manager.domain.dao.TransferDao;
import com.trxmanager.manager.domain.service.TransferService;
import com.trxmanager.manager.domain.vo.Transfer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.function.Predicate;

import static com.trxmanager.manager.util.Functional.fieldNonNull;
import static com.trxmanager.manager.util.Functional.numericFieldGt;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class AppTransferService {

    private final TransferDao transferDao;
    private final TransferService transferService;

    public Transfer findById(Long id) {
        return transferDao.findById(id);
    }

    public Transfer create(InputTransfer inputTransfer) {
        boolean valid = transferFilter().test(inputTransfer);
        if (!valid) {
            throw new InvalidValueException("Invalid value " + inputTransfer.toString());
        }

        Transfer transfer = transferDao.create(mapToTransfer(inputTransfer));
        log.info("Created new transfer {}", transfer);

        transferService.doTransfer(transfer);
        return withStatus(transfer, Transfer.Status.SUCCEEDED);
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

    private Transfer withStatus(Transfer transfer, Transfer.Status newStatus) {
        return transfer.toBuilder().status(newStatus).build();
    }
}
