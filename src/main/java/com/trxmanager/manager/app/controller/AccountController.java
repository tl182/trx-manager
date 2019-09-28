package com.trxmanager.manager.app.controller;

import com.google.gson.Gson;
import com.google.inject.Inject;
import com.trxmanager.manager.app.dto.NewAccount;
import com.trxmanager.manager.domain.dao.AccountDao;
import com.trxmanager.manager.domain.value.Account;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import spark.Request;
import spark.Response;

import java.math.BigDecimal;
import java.util.Optional;

import static com.trxmanager.manager.util.Const.ContentType.APPLICATION_JSON;
import static com.trxmanager.manager.util.Const.HttpStatus.BAD_REQUEST;
import static spark.Spark.*;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class AccountController implements Controller {

    private final Gson gson;
    private final AccountDao accountDao;

    @Override
    public void registerRoutes() {
        path("/accounts", () -> {
            get("/:id", this::getAccount, gson::toJson);
            post("", APPLICATION_JSON, this::createAccount, gson::toJson);
        });
    }

    private Account getAccount(Request req, Response res) {
        res.type(APPLICATION_JSON);

        Long id = parseId(req.params(":id"))
                .orElseThrow(() -> halt(BAD_REQUEST, gson.toJson("Invalid id value")));

        return accountDao
                .findById(id)
                .orElseThrow(() -> halt(BAD_REQUEST, gson.toJson("Account not found")));

    }

    private Account createAccount(Request req, Response res) {
        res.type(APPLICATION_JSON);

        BigDecimal balance = parseNewAccount(req.body())
                .map(NewAccount::getBalance)
                .orElseThrow(() -> halt(BAD_REQUEST, "Invalid new account value"));

        return accountDao.create(balance);
    }

    private Optional<Long> parseId(String id) {
        try {
            return Optional.of(Long.valueOf(id));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    private Optional<NewAccount> parseNewAccount(String json) {
        try {
            return Optional.ofNullable(gson.fromJson(json, NewAccount.class));
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
