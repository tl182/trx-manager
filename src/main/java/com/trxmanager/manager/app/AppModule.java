package com.trxmanager.manager.app;

import com.google.gson.Gson;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.multibindings.Multibinder;
import com.trxmanager.manager.app.controller.AccountController;
import com.trxmanager.manager.app.controller.Controller;
import com.trxmanager.manager.app.controller.HelloController;
import com.trxmanager.manager.app.service.AppAccountService;

public class AppModule extends AbstractModule {

    @Override
    protected void configure() {
        Multibinder<Controller> controllerMultibinder = Multibinder.newSetBinder(binder(), Controller.class);

        controllerMultibinder
                .addBinding()
                .to(HelloController.class)
                .in(Singleton.class);

        controllerMultibinder
                .addBinding()
                .to(AccountController.class)
                .in(Singleton.class);
        bind(AppAccountService.class).in(Singleton.class);

        bind(AppConfig.class).in(Singleton.class);
    }

    @Provides
    public Gson gson() {
        return new Gson();
    }
}