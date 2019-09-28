package com.trxmanager.manager.app.dto;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder
public class NewAccount {
    private BigDecimal balance;
}
