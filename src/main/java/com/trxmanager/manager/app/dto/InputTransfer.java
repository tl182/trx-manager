package com.trxmanager.manager.app.dto;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder
public class InputTransfer {
    private Long fromId;
    private Long toId;
    private BigDecimal amount;
}
