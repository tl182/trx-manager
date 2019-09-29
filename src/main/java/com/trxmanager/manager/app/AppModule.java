package com.trxmanager.manager.app;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.multibindings.Multibinder;
import com.trxmanager.manager.app.controller.AccountController;
import com.trxmanager.manager.app.controller.Controller;
import com.trxmanager.manager.app.controller.ControllerContainer;
import com.trxmanager.manager.app.controller.TransferController;
import com.trxmanager.manager.app.service.AppAccountService;
import com.trxmanager.manager.app.service.AppTransferService;

public class AppModule extends AbstractModule {

    @Override
    protected void configure() {
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
