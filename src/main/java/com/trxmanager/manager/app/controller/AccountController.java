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

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class AccountController extends JsonController {

    private static final String CONTEXT_ROUTE = "/accounts";

    private final AppAccountService accountService;

    @Override
    protected String getContextRoute() {
        return CONTEXT_ROUTE;
    }

    @Override
    public void registerRoutes() {
        getJson("/:id", this::getAccount);
        postJson(this::createAccount);
        putJson("/:id", this::updateAccount);
    }

    private Account getAccount(Request req, Response res) {
        Long id = parseId(req.params(":id"));
        return accountService.findById(id);
    }

    private Account createAccount(Request req, Response res) {
        InputAccount inputAccount = Conversions.fromJson(req.body(), InputAccount.class);
        return accountService.create(inputAccount);
    }

    private Account updateAccount(Request req, Response res) {
        Long id = parseId(req.params(":id"));
        InputAccount inputAccount = Conversions.fromJson(req.body(), InputAccount.class);
        return accountService.update(id, inputAccount);
    }
}
