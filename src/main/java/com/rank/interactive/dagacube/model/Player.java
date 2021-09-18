package com.rank.interactive.dagacube.model;

import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "players")
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "balance")
    private Double balance;

    @Column(name = "updated_date_time")
    private Timestamp updatedDateTime;

    @Column(name = "promo_applied")
    private boolean promoApplied;

    @Column(name = "promo_count")
    private int promoCount;

}
