package com.trxmanager.manager.app.controller;

import com.google.inject.Inject;
import com.trxmanager.manager.app.dto.InputTransfer;
import com.trxmanager.manager.app.service.AppTransferService;
import com.trxmanager.manager.domain.vo.Transfer;
import com.trxmanager.manager.util.Conversions;
import lombok.RequiredArgsConstructor;
import spark.Request;
import spark.Response;

import static com.trxmanager.manager.util.Const.ContentType.APPLICATION_JSON;
import static com.trxmanager.manager.util.Const.HttpStatus.BAD_REQUEST;
import static spark.Spark.*;

@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class TransferController implements Controller {

    private final AppTransferService transferService;

    @Override
    public void registerRoutes() {
        path("/transfers", () -> {
            get("/:id", this::getTransfer, Conversions::toJson);
            post("", APPLICATION_JSON, this::createTransfer, Conversions::toJson);
        });
    }

    private Transfer getTransfer(Request req, Response res) {
        res.type(APPLICATION_JSON);

        Long id = Conversions.parseLong(req.params(":id"))
                .orElseThrow(() -> halt(BAD_REQUEST, Conversions.toJson("Bad id format")));

        return transferService
                .findById(id)
                .orElseThrow(() -> halt(BAD_REQUEST, Conversions.toJson("Could not find transfer for given input")));
    }

    private Transfer createTransfer(Request req, Response res) {
        res.type(APPLICATION_JSON);

        InputTransfer inputTransfer = Conversions.fromJson(req.body(), InputTransfer.class)
                .orElseThrow(() -> halt(BAD_REQUEST, Conversions.toJson("Bad account value")));

        return transferService.create(inputTransfer)
                .orElseThrow(() -> halt(BAD_REQUEST, Conversions.toJson("Could not create transfer for given input value")));
    }
}
