package com.trxmanager.manager.domain.service;

import com.google.inject.Inject;
import com.trxmanager.manager.domain.dao.TransferDao;
import lombok.AllArgsConstructor;

@AllArgsConstructor(onConstructor = @__(@Inject))
public class TransferService {

    private final TransferDao transferDao;
}
