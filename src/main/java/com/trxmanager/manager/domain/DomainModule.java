package com.trxmanager.manager.domain;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.trxmanager.manager.domain.dao.AccountDao;

public class DomainModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(AccountDao.class).in(Singleton.class);
    }
}
