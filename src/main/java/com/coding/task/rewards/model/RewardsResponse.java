package com.coding.task.rewards.model;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class RewardsResponse {

    private int customerId;

    private Map<String, Integer> rewardsByMonth;

    private int totalRewards;
}
