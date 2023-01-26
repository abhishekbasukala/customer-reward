package com.coding.task.rewards.service;

import com.coding.task.rewards.dao.TransactionDAO;
import com.coding.task.rewards.exception.RewardsException;
import com.coding.task.rewards.model.RewardsResponse;
import com.coding.task.rewards.model.Transaction;
import com.coding.task.rewards.repository.TransactionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class RewardsServiceImpl implements RewardsService {

    private TransactionDAO transactionDAO;
    private TransactionRepository transactionRepository;

    @Autowired
    public RewardsServiceImpl(TransactionDAO transactionDAO, TransactionRepository transactionRepository) {
        this.transactionDAO = transactionDAO;
        this.transactionRepository = transactionRepository;
    }

    public RewardsServiceImpl() {

    }

    @Override
    public RewardsResponse calculateRewardsForCustomer(int customerId) throws Exception {
        log.info("Calculate rewards for customerId: {}", customerId);
        List<Transaction> transactions = transactionDAO.getTransactionsByCustomerId(customerId);
        if (transactions.size() > 0) {
            Map<String, Integer> monthlyPoints = getMonthlyPoints(transactions);
            return getMonthlyPointsForAllCustomers(monthlyPoints).get(0);
        }
        throw new RewardsException(HttpStatus.BAD_REQUEST, "No transactions found for customerId: " + customerId);
    }

    @Override
    public List<RewardsResponse> calculateRewards() throws Exception {
        log.info("Calculate rewards for all customer");
        List<Transaction> transactions = transactionRepository.findAll();
        Map<String, Integer> monthlyPoints = getMonthlyPoints(transactions);
        return getMonthlyPointsForAllCustomers(monthlyPoints);
    }

    private Map<String, Integer> getMonthlyPoints(List<Transaction> transactions) {
        Map<String, Integer> monthlyPoints = new HashMap<>();
        for (Transaction transaction : transactions) {
            int points = 0;
            if (transaction.getAmount() > 100) {
                points += 2 * (transaction.getAmount() - 100);
            }
            if (transaction.getAmount() > 50) {
                points += 1 * Math.min(transaction.getAmount() - 50, 50);
            }
            String key = transaction.getCustomer().getCustomerId() + "-" + transaction.getMonth().name();
            if (!monthlyPoints.containsKey(key)) {
                monthlyPoints.put(key, points);
            } else {
                monthlyPoints.put(key, monthlyPoints.get(key) + points);
            }
        }
        return monthlyPoints;
    }

    private List<RewardsResponse> getMonthlyPointsForAllCustomers(Map<String, Integer> monthlyPoints) {
        Map<String, Map<String, Integer>> monthlyPointsForCustomer = new HashMap<>();
        Map<String, Integer> totalPoints = new HashMap<>();
        for (String key : monthlyPoints.keySet()) {
            String[] parts = key.split("-");
            String customerId = parts[0];
            String month = parts[1];
            int points = monthlyPoints.get(key);
            if (!monthlyPointsForCustomer.containsKey(customerId)) {
                Map<String, Integer> monthMap = new HashMap<>();
                monthMap.put(month, points);
                monthlyPointsForCustomer.put(customerId, monthMap);
                totalPoints.put(customerId, points);
            } else {
                monthlyPointsForCustomer.get(customerId).put(month, monthlyPoints.get(key));
                totalPoints.put(customerId, totalPoints.get(customerId) + points);
            }
        }
        List<RewardsResponse> rewardsResponses = getRewardsResponses(monthlyPointsForCustomer, totalPoints);
        return rewardsResponses;
    }

    private List<RewardsResponse> getRewardsResponses(Map<String, Map<String, Integer>> monthlyPointsForCustomer, Map<String, Integer> totalPoints) {
        List<RewardsResponse> rewardsResponses = new ArrayList<>();
        for (String customerId : monthlyPointsForCustomer.keySet()) {
            RewardsResponse rewardsResponse = RewardsResponse.builder()
                    .rewardsByMonth(monthlyPointsForCustomer.get(customerId))
                    .totalRewards(totalPoints.get(customerId))
                    .customerId(Integer.valueOf(customerId))
                    .build();
            rewardsResponses.add(rewardsResponse);
        }
        return rewardsResponses;
    }
}
