package com.trxmanager.manager.app.controller;

import com.google.inject.Inject;
import com.trxmanager.manager.app.dto.InputAccount;
import com.trxmanager.manager.app.service.AppAccountService;
import com.trxmanager.manager.domain.vo.Account;
import com.trxmanager.manager.util.Conversions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import spark.Request;
import spark.Response;

import static com.trxmanager.manager.util.Const.ContentType.APPLICATION_JSON;
import static com.trxmanager.manager.util.Const.HttpStatus.BAD_REQUEST;
import static spark.Spark.*;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class AccountController implements Controller {

    private final AppAccountService accountService;

    @Override
    public void registerRoutes() {
        path("/accounts", () -> {
            get("/:id", this::getAccount, Conversions::toJson);
            post("", APPLICATION_JSON, this::createAccount, Conversions::toJson);
            put("/:id", APPLICATION_JSON, this::updateAccount, Conversions::toJson);
        });
    }

    private Account getAccount(Request req, Response res) {
        res.type(APPLICATION_JSON);

        Long id = Conversions.parseLong(req.params(":id"))
                .orElseThrow(() -> halt(BAD_REQUEST, Conversions.toJson("Bad id format")));

        return accountService
                .findById(id)
                .orElseThrow(() -> halt(BAD_REQUEST, Conversions.toJson("Could not find account for given input")));
    }

    private Account createAccount(Request req, Response res) {
        res.type(APPLICATION_JSON);

        InputAccount inputAccount = Conversions.fromJson(req.body(), InputAccount.class)
                .orElseThrow(() -> halt(BAD_REQUEST, Conversions.toJson("Bad account value")));

        return accountService.create(inputAccount)
                .orElseThrow(() -> halt(BAD_REQUEST, Conversions.toJson("Could not create account for given input value")));
    }

    private Account updateAccount(Request req, Response res) {
        res.type(APPLICATION_JSON);

        Long id = Conversions.parseLong(req.params(":id"))
                .orElseThrow(() -> halt(BAD_REQUEST, Conversions.toJson("Bad id format")));

        InputAccount inputAccount = Conversions.fromJson(req.body(), InputAccount.class)
                .orElseThrow(() -> halt(BAD_REQUEST, Conversions.toJson("Bad account value")));

        return accountService.update(id, inputAccount)
                .orElseThrow(() -> halt(BAD_REQUEST, Conversions.toJson("Could not update account for given input value")));
    }
}
