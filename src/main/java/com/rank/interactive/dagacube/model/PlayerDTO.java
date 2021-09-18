package com.rank.interactive.dagacube.model;

import lombok.Getter;

@Getter
public class PlayerDTO {
    private Double amount;
    private Long playerId;
    private Long transactionId;
    private String promoCode;
    private String username;
    private String password;
}
