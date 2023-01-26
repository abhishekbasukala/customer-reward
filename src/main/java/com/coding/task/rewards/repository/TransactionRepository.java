package com.coding.task.rewards.repository;

import com.coding.task.rewards.model.Customer;
import com.coding.task.rewards.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    List<Transaction> findAllByCustomer(Customer customer);

}
