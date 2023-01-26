package com.coding.task.rewards.service;

import com.coding.task.rewards.dao.TransactionDAO;
import com.coding.task.rewards.exception.RewardsException;
import com.coding.task.rewards.model.Customer;
import com.coding.task.rewards.model.Month;
import com.coding.task.rewards.model.RewardsResponse;
import com.coding.task.rewards.model.Transaction;
import com.coding.task.rewards.repository.TransactionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@SpringBootTest
class RewardsServiceImplTest {

    @InjectMocks
    private RewardsService rewardsService = new RewardsServiceImpl();

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private TransactionDAO transactionDAO;

    @BeforeEach
    void before() throws Exception {
        when(transactionRepository.findAll()).thenReturn(getAllTransactions());
        when(transactionDAO.getTransactionsByCustomerId(anyInt())).thenReturn(getTransactionForCustomer());
    }

    @DisplayName("Test calculate reward for any particular customer")
    @Test
    public void calculateRewardsForCustomer() throws Exception {
        RewardsResponse response = rewardsService.calculateRewardsForCustomer(1001);
        Assertions.assertEquals(115, response.getTotalRewards());
        Assertions.assertEquals(90, response.getRewardsByMonth().get(Month.JANUARY.name()));
        Assertions.assertEquals(25, response.getRewardsByMonth().get(Month.FEBRUARY.name()));
    }

    @DisplayName("Test calculate reward for any particular customer throws exception")
    @Test
    public void calculateRewardsForCustomer_throwsException() throws Exception {
        when(transactionDAO.getTransactionsByCustomerId(anyInt())).thenReturn(new ArrayList<>());
        Throwable exception = assertThrows(RewardsException.class, () -> {
            rewardsService.calculateRewardsForCustomer(1002);
        });
        assertEquals("No transactions found for customerId: 1002", exception.getMessage());
    }

    @DisplayName("Test calculate reward for all customers")
    @Test
    public void calculateRewards() throws Exception {
        List<RewardsResponse> response = rewardsService.calculateRewards();
        Optional<RewardsResponse> customerOneRewards = response.stream().filter(item -> item.getCustomerId() == 1001).findFirst();
        Optional<RewardsResponse> customerTwoRewards = response.stream().filter(item -> item.getCustomerId() == 1002).findFirst();

        assertAll(() -> assertEquals(115, customerOneRewards.get().getTotalRewards()),
                () -> assertEquals(90, customerOneRewards.get().getRewardsByMonth().get(Month.JANUARY.name())),
                () -> assertEquals(25, customerOneRewards.get().getRewardsByMonth().get(Month.FEBRUARY.name())));

        assertAll(() -> assertEquals(700, customerTwoRewards.get().getTotalRewards()),
                () -> assertEquals(700, customerTwoRewards.get().getRewardsByMonth().get(Month.MARCH.name())));

    }

    private List<Transaction> getTransactionForCustomer() {
        List<Transaction> transactions = new ArrayList<>();
        Customer customerOne = Customer.builder().customerId(1001).build();
        Transaction januaryTransaction = generateTransaction(customerOne, 120, Month.JANUARY);
        Transaction februaryTransaction = generateTransaction(customerOne, 75, Month.FEBRUARY);
        transactions.addAll(List.of(januaryTransaction, februaryTransaction));
        return transactions;
    }

    private List<Transaction> getAllTransactions() {
        List<Transaction> transactions = getTransactionForCustomer();
        Customer customerTwo = Customer.builder().customerId(1002).build();
        Transaction marchTransactionOne = generateTransaction(customerTwo, 200, Month.MARCH);
        Transaction marchTransactionTwo = generateTransaction(customerTwo, 300, Month.MARCH);
        transactions.add(marchTransactionOne);
        transactions.add(marchTransactionTwo);
        return transactions;
    }

    private Transaction generateTransaction(Customer customer, int amount, Month month) {
        return Transaction.builder()
                .customer(customer)
                .amount(amount)
                .month(month)
                .build();
    }

}