package com.trxmanager.manager.domain.dao;

import com.google.inject.Inject;
import com.trxmanager.manager.domain.generated.tables.records.AccountRecord;
import com.trxmanager.manager.domain.vo.Account;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;

import java.util.Optional;

import static com.trxmanager.manager.domain.generated.tables.Account.ACCOUNT;

@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class AccountDao {

    private final DSLContext ctx;

    public Optional<Account> findById(Long id) {
        AccountRecord accountRecord = ctx.fetchOne(ACCOUNT, ACCOUNT.ID.eq(id));
        return Optional.ofNullable(accountRecord).map(this::mapToAccount);
    }

    public Account create(Account account) {
        AccountRecord accountRecord = ctx.newRecord(ACCOUNT);
        accountRecord.setBalance(account.getBalance());
        accountRecord.store();
        return mapToAccount(accountRecord);
    }

    public Optional<Account> update(Account account) {
        return ctx.transactionResult(config -> {
            DSLContext dsl = config.dsl();
            AccountRecord accountRecord = dsl.fetchOne(ACCOUNT, ACCOUNT.ID.eq(account.getId()));
            return Optional.ofNullable(accountRecord)
                    .map(record -> {
                        record.setBalance(account.getBalance());
                        record.store();
                        return record;
                    })
                    .map(this::mapToAccount);
        });
    }

    private Account mapToAccount(AccountRecord accountRecord) {
        return Account.builder()
                .id(accountRecord.getId())
                .balance(accountRecord.getBalance())
                .build();
    }
}
