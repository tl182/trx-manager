package com.trxmanager.manager.domain.dao;

import com.google.inject.Inject;
import com.trxmanager.manager.domain.exception.RecordNotFoundException;
import com.trxmanager.manager.domain.exception.TransferCreationException;
import com.trxmanager.manager.domain.generated.tables.records.TransferRecord;
import com.trxmanager.manager.domain.vo.Transfer;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.exception.DataAccessException;

import static com.trxmanager.manager.domain.generated.tables.Transfer.TRANSFER;

@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class TransferDao {

    private final DSLContext ctx;

    public Transfer findById(Long id) {
        TransferRecord transferRecord = ctx.fetchOne(TRANSFER, TRANSFER.ID.eq(id));
        if (transferRecord == null) {
            throw new RecordNotFoundException("Transfer with id " + id + " not found");
        }
        return mapToTransfer(transferRecord);
    }

    public Transfer create(Transfer transfer) {
        try {
            TransferRecord transferRecord = ctx.newRecord(TRANSFER);
            transferRecord.setFromId(transfer.getFromId());
            transferRecord.setToId(transfer.getToId());
            transferRecord.setAmount(transfer.getAmount());
            transferRecord.setStatus(Transfer.Status.CREATED.name());
            transferRecord.store();
            return mapToTransfer(transferRecord);
        } catch (DataAccessException e) {
            throw new TransferCreationException("Could not create transfer", e);
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
