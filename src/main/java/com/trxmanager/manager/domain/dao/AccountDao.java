package com.trxmanager.manager.domain.dao;

import com.google.inject.Inject;
import com.trxmanager.manager.domain.generated.tables.records.AccountRecord;
import com.trxmanager.manager.domain.value.Account;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;

import java.math.BigDecimal;
import java.util.Optional;

import static com.trxmanager.manager.domain.generated.tables.Account.ACCOUNT;

@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class AccountDao {

    private final DSLContext ctx;

    public Optional<Account> findById(Long id) {
        AccountRecord accountRecord = ctx.fetchOne(ACCOUNT, ACCOUNT.ID.eq(id));
        return Optional.ofNullable(accountRecord).map(this::mapToAccount);
    }

    public Account create(BigDecimal newBalance) {
        AccountRecord accountRecord = ctx.newRecord(ACCOUNT);
        accountRecord.setBalance(newBalance);
        accountRecord.store();
        return mapToAccount(accountRecord);
    }

    private Account mapToAccount(@NonNull AccountRecord accountRecord) {
        return Account.builder()
                .id(accountRecord.getId())
                .balance(accountRecord.getBalance())
                .build();
    }
}
