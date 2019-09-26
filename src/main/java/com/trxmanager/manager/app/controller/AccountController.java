package com.trxmanager.manager.app.controller;

import com.google.gson.Gson;
import com.google.inject.Inject;
import com.trxmanager.manager.app.dto.AppAccount;
import com.trxmanager.manager.app.service.AppAccountService;
import lombok.extern.slf4j.Slf4j;
import spark.Request;
import spark.Response;

import static com.trxmanager.manager.util.Const.ContentType.APPLICATION_JSON;
import static com.trxmanager.manager.util.Const.HttpStatus.BAD_REQUEST;
import static spark.Spark.*;

@Slf4j
public class AccountController implements Controller {

    private final Gson gson;
    private final AppAccountService appAccountService;

    @Inject
    public AccountController(Gson gson, AppAccountService appAccountService) {
        this.gson = gson;
        this.appAccountService = appAccountService;
    }

    @Override
    public void registerRoutes() {
        path("/accounts", () -> {
            internalServerError((req, res) -> gson.toJson("Internal server error"));
            notFound((req, res) -> gson.toJson("Not found"));

            get("/:id", this::getAccount, gson::toJson);
            post("", APPLICATION_JSON, this::createAccount, gson::toJson);
        });
    }

    private AppAccount getAccount(Request req, Response res) {
        res.type(APPLICATION_JSON);

        Long id = Long.valueOf(req.params(":id"));
        return appAccountService
                .findById(id)
                .orElseThrow(() -> halt(BAD_REQUEST, gson.toJson("Account not found")));
    }

    private AppAccount createAccount(Request req, Response res) {
        res.type(APPLICATION_JSON);

        AppAccount newAccount = gson.fromJson(req.body(), AppAccount.class);
        return appAccountService.create(newAccount);
    }
}
