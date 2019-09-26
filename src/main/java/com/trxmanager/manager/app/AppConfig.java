package com.trxmanager.manager.app;

import com.google.inject.Inject;
import com.trxmanager.manager.app.controller.Controller;

import java.util.Set;

import static spark.Spark.port;
import static spark.Spark.threadPool;

public class AppConfig {

    private final Set<Controller> controllers;

    @Inject
    public AppConfig(Set<Controller> controllers) {
        this.controllers = controllers;
    }

    public void init(int port) {
        port(port);
        threadPool(10);

        controllers.forEach(Controller::registerRoutes);
    }
}
