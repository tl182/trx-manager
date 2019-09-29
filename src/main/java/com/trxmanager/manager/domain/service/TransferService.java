package com.trxmanager.manager.domain.service;

import com.google.inject.Inject;
import com.trxmanager.manager.domain.exception.NotEnoughFundsException;
import com.trxmanager.manager.domain.exception.TransferExecutionException;
import com.trxmanager.manager.domain.vo.Transfer;
import com.trxmanager.manager.util.retry.MaxNumberOfRetriesException;
import com.trxmanager.manager.util.retry.Retry;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.jooq.exception.DataAccessException;

import java.math.BigDecimal;

import static com.trxmanager.manager.domain.generated.tables.Account.ACCOUNT;
import static com.trxmanager.manager.domain.generated.tables.Transfer.TRANSFER;

@Slf4j
@AllArgsConstructor(onConstructor = @__(@Inject))
public class TransferService {

    private static final int RETRY_COUNT = 3;
    private static final int DELAY = 1000;

    private final DSLContext dslContext;

    public void doTransfer(Transfer transfer) {
        Long fromId = transfer.getFromId();
        Long toId = transfer.getToId();
        BigDecimal amount = transfer.getAmount();

        log.info("Starting transfer from account with id {} to account with id {} amount {}", fromId, toId, amount);

        try {
            Retry.builder()
                    .retryCount(RETRY_COUNT)
                    .delayMillis(DELAY)
                    .retryException(DataAccessException.class)
                    .passException(NotEnoughFundsException.class)
                    .build()
                    .execute(() -> transferFunds(transfer));
        } catch (MaxNumberOfRetriesException e) {
            setStatusFailed(transfer.getId());
            throw new TransferExecutionException("Could not transfer from account with id " + fromId
                    + " to account with id " + toId + " amount " + amount, e.getCause());
        } catch (Exception e) {
            setStatusFailed(transfer.getId());
            throw e;
        }
    }

    private void transferFunds(Transfer transfer) {
        Long id = transfer.getId();
        Long fromId = transfer.getFromId();
        Long toId = transfer.getToId();
        BigDecimal amount = transfer.getAmount();

        dslContext.transaction(config -> {
            DSLContext dsl = config.dsl();

            BigDecimal senderBalance = dsl.select(ACCOUNT.BALANCE)
                    .from(ACCOUNT)
                    .where(ACCOUNT.ID.eq(fromId))
                    .fetchOne()
                    .value1();

            BigDecimal newSenderBalance = senderBalance.subtract(amount);

            if (newSenderBalance.compareTo(BigDecimal.ZERO) < 0) {
                throw new NotEnoughFundsException("Account with id " + fromId + " does not have enough funds to execute transfer");
            }

            BigDecimal receiverBalance = dsl.select(ACCOUNT.BALANCE)
                    .from(ACCOUNT)
                    .where(ACCOUNT.ID.eq(toId))
                    .fetchOne()
                    .value1();

            BigDecimal newReceiverBalance = receiverBalance.add(amount);

            dsl.update(ACCOUNT).set(ACCOUNT.BALANCE, newSenderBalance).where(ACCOUNT.ID.eq(fromId)).execute();
            dsl.update(ACCOUNT).set(ACCOUNT.BALANCE, newReceiverBalance).where(ACCOUNT.ID.eq(toId)).execute();
            dsl.update(TRANSFER)
                    .set(TRANSFER.STATUS, Transfer.Status.SUCCEEDED.name())
                    .where(TRANSFER.ID.eq(id))
                    .execute();

            log.info("Transferred from account with id {} to account with id {} amount {}", fromId, toId, amount);
        });
    }

    private void setStatusFailed(Long transferId) {
        log.info("Setting status {} for transfer with id {}", Transfer.Status.FAILED.name(), transferId);
        dslContext.update(TRANSFER)
                .set(TRANSFER.STATUS, Transfer.Status.FAILED.name())
                .where(TRANSFER.ID.eq(transferId))
                .execute();
    }
}
