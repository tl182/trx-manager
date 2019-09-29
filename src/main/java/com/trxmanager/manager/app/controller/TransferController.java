package com.trxmanager.manager.app.controller;

import com.google.inject.Inject;
import com.trxmanager.manager.app.dto.InputTransfer;
import com.trxmanager.manager.app.service.AppTransferService;
import com.trxmanager.manager.domain.vo.Transfer;
import com.trxmanager.manager.util.Conversions;
import lombok.RequiredArgsConstructor;
import spark.Request;
import spark.Response;

@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class TransferController extends JsonController {

    private static final String CONTEXT_ROUTE = "/transfers";

    private final AppTransferService transferService;

    @Override
    protected String getContextRoute() {
        return CONTEXT_ROUTE;
    }

    @Override
    public void registerRoutes() {
        getJson("/:id", this::getTransfer);
        postJson(this::createTransfer);
    }

    private Transfer getTransfer(Request req, Response res) {
        Long id = parseId(req.params(":id"));
        return transferService.findById(id);
    }

    private Transfer createTransfer(Request req, Response res) {
        InputTransfer inputTransfer = Conversions.fromJson(req.body(), InputTransfer.class);
        return transferService.create(inputTransfer);
    }
}
