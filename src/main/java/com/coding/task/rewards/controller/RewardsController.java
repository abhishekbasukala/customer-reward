package com.coding.task.rewards.controller;

import com.coding.task.rewards.model.RewardsResponse;
import com.coding.task.rewards.service.RewardsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/rewards")
public class RewardsController {

    @Autowired
    private RewardsService rewardsService;

    @Operation(summary = "Calculate rewards for given customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rewards calculated for given customer",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = RewardsResponse.class))}),
            @ApiResponse(responseCode = "400", description = "No transactions found for given customer id",
                    content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<RewardsResponse> getRewardsForCustomer(@PathVariable("id") int customerId)
            throws Exception {
        return ResponseEntity.ok(rewardsService.calculateRewardsForCustomer(customerId));
    }

    @Operation(summary = "Calculate rewards for all customers")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rewards calculated for all customers",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = RewardsResponse.class))})
    })
    @GetMapping
    public ResponseEntity<List<RewardsResponse>> getRewards() throws Exception {
        return ResponseEntity.ok(rewardsService.calculateRewards());
    }
}
