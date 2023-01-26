package com.coding.task.rewards.service;

import com.coding.task.rewards.model.RewardsResponse;

import java.util.List;

public interface RewardsService {

    public RewardsResponse calculateRewardsForCustomer(int customerId) throws Exception;

    public List<RewardsResponse> calculateRewards() throws Exception;
}
