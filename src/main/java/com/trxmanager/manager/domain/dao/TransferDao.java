package com.trxmanager.manager.domain.dao;

import com.google.inject.Inject;
import com.trxmanager.manager.domain.generated.tables.records.TransferRecord;
import com.trxmanager.manager.domain.vo.Transfer;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.exception.DataAccessException;

import java.util.Optional;

import static com.trxmanager.manager.domain.generated.tables.Transfer.TRANSFER;

@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class TransferDao {

    private final DSLContext ctx;

    public Optional<Transfer> findById(Long id) {
        TransferRecord transferRecord = ctx.fetchOne(TRANSFER, TRANSFER.ID.eq(id));
        return Optional.ofNullable(transferRecord).map(this::mapToTransfer);
    }

    public Optional<Transfer> create(Transfer transfer) {
        try {
            TransferRecord transferRecord = ctx.newRecord(TRANSFER);
            transferRecord.setFromId(transfer.getFromId());
            transferRecord.setToId(transfer.getToId());
            transferRecord.setAmount(transfer.getAmount());
            transferRecord.setStatus(Transfer.Status.CREATED.name());
            transferRecord.store();
            return Optional.of(transferRecord).map(this::mapToTransfer);
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    private Transfer mapToTransfer(TransferRecord transferRecord) {
        return Transfer.builder()
                .id(transferRecord.getId())
                .fromId(transferRecord.getFromId())
                .toId(transferRecord.getToId())
                .amount(transferRecord.getAmount())
                .status(Transfer.Status.valueOf(transferRecord.getStatus()))
                .build();
    }
}
