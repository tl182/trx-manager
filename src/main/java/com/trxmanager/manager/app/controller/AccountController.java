package com.trxmanager.manager.app.controller;

import com.google.gson.Gson;
import com.google.inject.Inject;
import com.trxmanager.manager.app.dto.InputAccount;
import com.trxmanager.manager.domain.dao.AccountDao;
import com.trxmanager.manager.domain.value.Account;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import spark.Request;
import spark.Response;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

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
            put("/:id", APPLICATION_JSON, this::updateAccount, gson::toJson);
        });
    }

    private Account getAccount(Request req, Response res) {
        res.type(APPLICATION_JSON);

        Long id = parseLong(req.params(":id"))
                .orElseThrow(() -> halt(BAD_REQUEST, gson.toJson("Invalid id value")));

        return accountDao
                .findById(id)
                .orElseThrow(() -> halt(BAD_REQUEST, gson.toJson("Account not found")));
    }

    private Account createAccount(Request req, Response res) {
        res.type(APPLICATION_JSON);

        InputAccount inputAccount = fromJson(req.body(), InputAccount.class)
                .filter(fieldNonNull(InputAccount::getBalance)
                        .and(numericFieldGt(InputAccount::getBalance, BigDecimal.ZERO)))
                .orElseThrow(() -> halt(BAD_REQUEST, "Invalid new account value"));

        return accountDao.create(Account.builder().balance(inputAccount.getBalance()).build());
    }

    private Account updateAccount(Request req, Response res) {
        res.type(APPLICATION_JSON);

        Long id = parseLong(req.params(":id"))
                .orElseThrow(() -> halt(BAD_REQUEST, gson.toJson("Invalid id value")));

        InputAccount inputAccount = fromJson(req.body(), InputAccount.class)
                .filter(fieldNonNull(InputAccount::getBalance)
                        .and(numericFieldGt(InputAccount::getBalance, BigDecimal.ZERO)))
                .orElseThrow(() -> halt(BAD_REQUEST, "Invalid account value"));

        return accountDao.update(Account.builder().id(id).balance(inputAccount.getBalance()).build())
                .orElseThrow(() -> halt(BAD_REQUEST, "Account not found"));
    }

    private static Optional<Long> parseLong(String id) {
        try {
            return Optional.of(Long.valueOf(id));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    private static <T> Optional<T> fromJson(String json, Class<T> tClass) {
        try {
            return Optional.ofNullable(new Gson().fromJson(json, tClass));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private static <T> Predicate<T> fieldNonNull(Function<T, ?> fieldExtractor) {
        return t -> safeGet(t, fieldExtractor).isPresent();
    }

    private static <T> Predicate<T> numericFieldGt(Function<T, BigDecimal> fieldExtractor, BigDecimal value) {
        return t -> safeGet(t, fieldExtractor)
                .filter(number -> number.compareTo(value) >= 0)
                .isPresent();
    }

    private static <T, S> Optional<S> safeGet(T t, Function<T, S> fieldExtractor) {
        try {
            return Optional.ofNullable(fieldExtractor.apply(t));
        } catch (NullPointerException e) {
            return Optional.empty();
        }
    }
}
