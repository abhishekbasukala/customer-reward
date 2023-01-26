package com.coding.task.rewards.controller;

import com.coding.task.rewards.CustomerRewardApplication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {CustomerRewardApplication.class})
@WebAppConfiguration
@TestPropertySource("classpath:application-test.properties")
class RewardsControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @DisplayName("Test servlet context is present")
    @Test
    public void givenWac_validateServletContext() {
        ServletContext servletContext = webApplicationContext.getServletContext();

        assertNotNull(servletContext);
        assertTrue(servletContext instanceof MockServletContext);
        assertNotNull(webApplicationContext.getBean("rewardsController"));
    }

    @DisplayName("Test get all rewards")
    @Test
    public void givenRewardsURI_returnOk() throws Exception {
        this.mockMvc.perform(get("/v1/rewards"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.customerId == 1002)].totalRewards").value(115));
    }

    @DisplayName("Test get rewards for specific customer")
    @Test
    public void givenRewardsURI_withPathVariable_returnOk() throws Exception {
        this.mockMvc.perform(get("/v1/rewards/{customerId}", 1004))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.customerId == 1004)].totalRewards").value(390))
                .andExpect(jsonPath("$[?(@.customerId == 1004)].rewardsByMonth.JANUARY").value(240))
                .andExpect(jsonPath("$[?(@.customerId == 1004)].rewardsByMonth.FEBRUARY").value(150));
    }

    @DisplayName("Test 400 Bad Request Scenario")
    @Test
    public void givenRewardsURI_withInvalidPathVariable_returnBadRequest() throws Exception {
        this.mockMvc.perform(get("/v1/rewards/{customerId}", 1008))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

}