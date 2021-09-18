package com.rank.interactive.dagacube.dao;

import com.rank.interactive.dagacube.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepository extends JpaRepository<Player, Long> {
    Player findById(long id);
    Player findByUsername(String username);
}
