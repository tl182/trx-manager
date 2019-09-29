package com.trxmanager.manager.domain;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.trxmanager.manager.domain.dao.AccountDao;
import com.trxmanager.manager.domain.dao.TransferDao;
import com.trxmanager.manager.domain.service.TransferService;

public class DomainModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(AccountDao.class).in(Singleton.class);
        bind(TransferDao.class).in(Singleton.class);
        bind(TransferService.class).in(Singleton.class);
    }
}
