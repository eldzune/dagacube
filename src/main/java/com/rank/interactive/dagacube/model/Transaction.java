package com.rank.interactive.dagacube.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Getter
@Setter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "transactions")
public class Transaction {
    @Id
    @Column(name="id")
    private Long id;

    @Column(name="player_id")
    private Long playerId;

    @Column(name="transaction")
    private String transaction;

    @Column(name="amount")
    private Double amount;

    @Column(name="created_date_time")
    private Timestamp createdDateTime;
}
