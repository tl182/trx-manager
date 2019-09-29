package com.trxmanager.manager.domain.dao;

import com.google.inject.Inject;
import com.trxmanager.manager.domain.exception.RecordNotFoundException;
import com.trxmanager.manager.domain.generated.tables.records.AccountRecord;
import com.trxmanager.manager.domain.vo.Account;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;

import static com.trxmanager.manager.domain.generated.tables.Account.ACCOUNT;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class AccountDao {

    private final DSLContext ctx;

    public Account findById(Long id) {
        AccountRecord accountRecord = ctx.fetchOne(ACCOUNT, ACCOUNT.ID.eq(id));
        if (accountRecord == null) {
            throw new RecordNotFoundException("Account with id " + id + " not found");
        }
        return mapToAccount(accountRecord);
    }

    public Account create(Account account) {
        AccountRecord accountRecord = ctx.newRecord(ACCOUNT);
        accountRecord.setBalance(account.getBalance());
        accountRecord.store();
        return mapToAccount(accountRecord);
    }

    public Account update(Account account) {
        return ctx.transactionResult(config -> {
            DSLContext dsl = config.dsl();

            Long id = account.getId();
            AccountRecord accountRecord = dsl.fetchOne(ACCOUNT, ACCOUNT.ID.eq(id));
            if (accountRecord == null) {
                throw new RecordNotFoundException("Account with id " + id + " not found");
            }

            accountRecord.setBalance(account.getBalance());
            accountRecord.store();

            return mapToAccount(accountRecord);
        });
    }

    private Account mapToAccount(AccountRecord accountRecord) {
        return Account.builder()
                .id(accountRecord.getId())
                .balance(accountRecord.getBalance())
                .build();
    }
}
