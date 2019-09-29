package com.trxmanager.manager.domain.vo;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder
public class Account {
    private Long id;
    private BigDecimal balance;
}
