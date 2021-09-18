package com.rank.interactive.dagacube.service;

import com.rank.interactive.dagacube.Constants;
import com.rank.interactive.dagacube.dao.PlayerRepository;
import com.rank.interactive.dagacube.dao.TransactionRepository;
import com.rank.interactive.dagacube.model.Player;
import com.rank.interactive.dagacube.model.PlayerDTO;
import com.rank.interactive.dagacube.model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.List;

@Service
@Transactional
public class DagacubeServiceImpl implements DagacubeService {

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    public Player getPlayerById(long id) {
        return playerRepository.findById(id);
    }

    public Player getPlayerByUsername(String username) {
        return playerRepository.findByUsername(username);
    }

    public void savePlayer(Player player) {
        playerRepository.save(player);
    }

    public Transaction getTransactionById(long id) {
        return transactionRepository.findById(id);
    }

    public void saveTransaction(Transaction transaction) {
        transactionRepository.save(transaction);
    }

    public List<Transaction> findLast10Transactions(long id) {
        return transactionRepository.findFirst10ByPlayerIdOrderByCreatedDateTimeDesc(id);
    }

    public ResponseEntity<String> processWager(@RequestBody PlayerDTO playerDTO, Player player) {
        boolean isPromoTransaction = false;

        //Check if amount is valid
        if (playerDTO.getAmount() <= 0) {
            return new ResponseEntity<>(Constants.INVALID_AMOUNT, HttpStatus.BAD_REQUEST);
        }

        //Check if promo code has been applied
        if (player.isPromoApplied()) {
            if (player.getPromoCount() < 5) {
                player.setPromoCount(player.getPromoCount() + 1);

                //Check if promo code has reached limit
                if (player.getPromoCount() == 5) {
                    //The last time the promo code is applied
                    player.setPromoApplied(false);
                }

                isPromoTransaction = true;
            }
        } else if (playerDTO.getAmount() <= player.getBalance()) {
            player.setBalance(player.getBalance() - playerDTO.getAmount());
        } else {
            return new ResponseEntity<>(Constants.WAGER_NO_BALANCE, HttpStatus.I_AM_A_TEAPOT);
        }

        player.setUpdatedDateTime(new Timestamp(System.currentTimeMillis()));

        //Save player and transaction
        savePlayer(player);
        saveTransaction(Transaction.builder()
                .id(playerDTO.getTransactionId())
                .transaction(isPromoTransaction ? Constants.WAGER_PROMO : Constants.WAGER)
                .createdDateTime(new Timestamp(System.currentTimeMillis()))
                .playerId(player.getId())
                .amount(playerDTO.getAmount())
                .build());

        return new ResponseEntity<>(Constants.WAGER_SUCCESS, HttpStatus.OK);
    }

    public ResponseEntity<String> checkPromoCode(@RequestBody PlayerDTO playerDTO, Player player) {
        //Check is promo code is available and if it has not been applied
        if (playerDTO.getPromoCode() != null && !player.isPromoApplied()) {
            //Check if promo code is valid and if it still applies
            if (playerDTO.getPromoCode().equals(Constants.PROMO_CODE) && player.getPromoCount() == 0) {
                player.setPromoApplied(true);
            } else if (player.getPromoCount() == 5) {
                return new ResponseEntity<>(Constants.INVALID_PROMO_COUNT, HttpStatus.BAD_REQUEST);
            } else {
                return new ResponseEntity<>(Constants.INVALID_PROMO_CODE, HttpStatus.BAD_REQUEST);
            }
        }
        return null;
    }

    public ResponseEntity<String> processDeposit(@RequestBody PlayerDTO playerDTO, Player player, Transaction transaction) {
        //Check if the transaction has been used before
        if (transaction != null) {
            return new ResponseEntity<>(Constants.TRANSACTION_USED, HttpStatus.IM_USED);
        }

        //Set player balance and update time
        player.setBalance(player.getBalance() + playerDTO.getAmount());
        player.setUpdatedDateTime(new Timestamp(System.currentTimeMillis()));

        //Set transaction
        transaction = Transaction.builder()
                .id(playerDTO.getTransactionId())
                .transaction(Constants.DEPOSIT)
                .playerId(player.getId())
                .amount(playerDTO.getAmount())
                .createdDateTime(new Timestamp(System.currentTimeMillis()))
                .build();

        //Save player and transaction
        savePlayer(player);
        saveTransaction(transaction);

        return new ResponseEntity<>(Constants.DEPOSIT_SUCCESS, HttpStatus.OK);
    }
}
