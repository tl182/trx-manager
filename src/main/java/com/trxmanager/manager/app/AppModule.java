package com.trxmanager.manager.app;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.multibindings.Multibinder;
import com.trxmanager.manager.app.controller.*;
import com.trxmanager.manager.app.service.AppAccountService;
import com.trxmanager.manager.app.service.AppTransferService;

public class AppModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(ExceptionMapper.class).in(Singleton.class);

        Multibinder<Controller> controllerMultibinder = Multibinder.newSetBinder(binder(), Controller.class);
        controllerMultibinder
                .addBinding()
                .to(AccountController.class)
                .in(Singleton.class);
        controllerMultibinder
                .addBinding()
                .to(TransferController.class)
                .in(Singleton.class);

        bind(ControllerContainer.class).in(Singleton.class);

        bind(AppAccountService.class).in(Singleton.class);
        bind(AppTransferService.class).in(Singleton.class);
    }
}
