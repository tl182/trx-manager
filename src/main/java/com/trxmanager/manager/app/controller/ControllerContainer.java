package com.trxmanager.manager.app.controller;

import com.google.inject.Inject;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class ControllerContainer {

    private final Set<Controller> controllers;

    public void initControllers() {
        controllers.forEach(Controller::registerRoutes);
    }
}
