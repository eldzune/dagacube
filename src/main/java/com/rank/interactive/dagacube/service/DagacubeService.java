package com.rank.interactive.dagacube.service;

import com.rank.interactive.dagacube.model.Player;
import com.rank.interactive.dagacube.model.PlayerDTO;
import com.rank.interactive.dagacube.model.Transaction;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface DagacubeService {
    Player getPlayerById(long id);

    Player getPlayerByUsername(String username);

    void saveTransaction(Transaction transaction);

    void savePlayer(Player player);

    List<Transaction> findLast10Transactions(long id);

    ResponseEntity<String> processWager(@RequestBody PlayerDTO playerDTO, Player player);

    ResponseEntity<String> checkPromoCode(@RequestBody PlayerDTO playerDTO, Player player);

    ResponseEntity<String> processDeposit(@RequestBody PlayerDTO playerDTO, Player player, Transaction transaction);
}
