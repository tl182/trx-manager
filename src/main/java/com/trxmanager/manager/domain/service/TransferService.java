package com.trxmanager.manager.domain.service;

import com.google.inject.Inject;
import com.trxmanager.manager.domain.exception.NotEnoughFundsException;
import com.trxmanager.manager.domain.vo.Transfer;
import com.trxmanager.manager.util.retry.MaxNumberOfRetriesException;
import com.trxmanager.manager.util.retry.Retry;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.jooq.exception.DataAccessException;

import java.math.BigDecimal;

import static com.trxmanager.manager.domain.generated.tables.Account.ACCOUNT;

@Slf4j
@AllArgsConstructor(onConstructor = @__(@Inject))
public class TransferService {

    private static final int RETRY_COUNT = 3;
    private static final int DELAY = 1000;

    private final DSLContext dslContext;

    public Transfer.Status doTransfer(Transfer transfer) {
        log.info("Starting transfer from account with id {} to account with id {} amount {}",
                transfer.getFromId(), transfer.getToId(), transfer.getAmount());

        try {
            Retry.builder()
                    .retryCount(RETRY_COUNT)
                    .delayMillis(DELAY)
                    .retryException(DataAccessException.class)
                    .passException(NotEnoughFundsException.class)
                    .build()
                    .execute(() -> transferFunds(transfer));
            return Transfer.Status.SUCCEEDED;
        } catch (MaxNumberOfRetriesException e) {
            log.error("Could not do transfer", e.getCause());
            return Transfer.Status.FAILED;
        }
    }

    private void transferFunds(Transfer transfer) {
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

            log.info("Transferred from account with id {} to account with id {} amount {}", fromId, toId, amount);
        });
    }
}
