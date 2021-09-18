package com.rank.interactive.dagacube.controller;

import com.rank.interactive.dagacube.Constants;
import com.rank.interactive.dagacube.model.Player;
import com.rank.interactive.dagacube.model.PlayerDTO;
import com.rank.interactive.dagacube.model.Transaction;
import com.rank.interactive.dagacube.service.DagacubeServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/v1/app")
public class DagacubeController {

    @Autowired
    private DagacubeServiceImpl dagacubeService;

    //Get Player balance
    @GetMapping("/player/balance/{id}")
    public ResponseEntity<String> getPlayerBalance(@PathVariable long id) {
        try {
            Double balance = dagacubeService.getPlayerById(id).getBalance();
            return new ResponseEntity<>(String.valueOf(balance), HttpStatus.OK);
        } catch (Exception exc) {
            log.info("{}", exc.getMessage());

            return new ResponseEntity<>(exc.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    //Place a wager and record transaction
    @PostMapping("/player/wager")
    public ResponseEntity<String> wagerMoney(@RequestBody PlayerDTO playerDTO) {
        try {
            Player player = dagacubeService.getPlayerById(playerDTO.getPlayerId());

            //Check if the player exists
            if (player == null) {
                return new ResponseEntity<>(Constants.NOT_EXIST, HttpStatus.BAD_REQUEST);
            }

            Transaction transaction = dagacubeService.getTransactionById(playerDTO.getTransactionId());

            //Check if the transaction has been used before
            if (transaction != null) {
                return new ResponseEntity<>(Constants.TRANSACTION_USED, HttpStatus.IM_USED);
            }

            //Check for and apply promo code
            ResponseEntity<String> response = dagacubeService.checkPromoCode(playerDTO, player);

            if (response != null) return response;

            //Check if the player has enough funds and process wager
            return dagacubeService.processWager(playerDTO, player);
        } catch (Exception exc) {
            log.info("{}", exc.getMessage());
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    //Deposit into players account and record transaction
    @PostMapping("/player/deposit")
    public ResponseEntity<String> depositMoney(@RequestBody PlayerDTO playerDTO) {
        try {
            Player player = dagacubeService.getPlayerById(playerDTO.getPlayerId());

            //Check if the player exists
            if (player == null) {
                return new ResponseEntity<>(Constants.NOT_EXIST, HttpStatus.BAD_REQUEST);
            }

            Transaction transaction = dagacubeService.getTransactionById(playerDTO.getTransactionId());

            return dagacubeService.processDeposit(playerDTO, player, transaction);

        } catch (Exception exc) {
            log.info("{}", exc.getMessage());
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    //Deposit into players account and record transaction
    @PostMapping("/admin/transaction/history")
    public ResponseEntity<List<Transaction>> getTransactionHistory(@RequestBody PlayerDTO playerDTO) {
        try {
            //Check password
            if (!playerDTO.getPassword().equals(Constants.PASSWORD)) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

            Player player = dagacubeService.getPlayerByUsername(playerDTO.getUsername());

            //Check if the player exists
            if (player == null) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            return new ResponseEntity<>(dagacubeService.findLast10Transactions(player.getId()), HttpStatus.OK);
        } catch (Exception exc) {
            log.info("{}", exc.getMessage());
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

}
