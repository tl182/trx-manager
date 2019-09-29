package com.trxmanager.manager.domain.vo;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder
public class Transfer {
    private Long id;
    private Long fromId;
    private Long toId;
    private BigDecimal amount;
    private Status status;

    public enum Status {
        CREATED, SUCCEEDED, FAILED;
    }
}
