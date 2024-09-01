package com.techlabs.app.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.techlabs.app.entity.Account;
import com.techlabs.app.entity.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

	Page<Transaction> findByTransactionDateBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

	Page<Transaction> findBySenderAccountNumberOrReceiverAccountNumber(int senderAccountNumber, int receiverAccountNumber, Pageable pageable);

	Page<Transaction> findBySenderAccountNumberInOrReceiverAccountNumberIn(List<Integer> accountNumbers,
			List<Integer> accountNumbers2, Pageable pageable);

	@Query("SELECT t FROM Transaction t WHERE (t.senderAccountNumber IN :accountNumbers OR t.receiverAccountNumber IN :accountNumbers2) AND t.transactionDate BETWEEN :startDate AND :endDate")
	Page<Transaction> findBySenderAccountNumberInOrReceiverAccountNumberInAndTransactionDateBetween(
			List<Integer> accountNumbers, List<Integer> accountNumbers2, LocalDateTime startDate, LocalDateTime endDate,
			Pageable pageable);

	boolean existsBySenderAccountNumberAndTransactionDateAfter(int accountNumber, LocalDateTime oneYearAgo);

	boolean existsBySenderAccountNumberOrReceiverAccountNumberAndTransactionDateAfter(int accountNumber,
			int accountNumber2, LocalDateTime oneYearAgo);

	List<Transaction> findBySenderAccountNumberInOrReceiverAccountNumberIn(List<Integer> accountNumbers,
			List<Integer> accountNumbers2);

	@Query("SELECT t FROM Transaction t WHERE (t.senderAccountNumber IN :accountNumbers OR t.receiverAccountNumber IN :accountNumbers2) AND t.transactionDate BETWEEN :startDate AND :endDate")
	List<Transaction> findBySenderAccountNumberInOrReceiverAccountNumberInAndTransactionDateBetween(
			List<Integer> accountNumbers, List<Integer> accountNumbers2, LocalDateTime startDate,
			LocalDateTime endDate);

	List<Transaction> findBySenderAccountNumberOrReceiverAccountNumber(int accountNumber, int accountNumber2);

}
