package com.rank.interactive.dagacube.dao;

import com.rank.interactive.dagacube.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Transaction findById(long id);
    List<Transaction> findFirst10ByPlayerIdOrderByCreatedDateTimeDesc(long id);
}
