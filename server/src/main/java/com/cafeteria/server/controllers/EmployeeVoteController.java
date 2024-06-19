package com.cafeteria.server.controllers;

import com.cafeteria.server.models.EmployeeVote;
import com.cafeteria.server.services.EmployeeVoteService;

import java.util.List;

public class EmployeeVoteController {
    private EmployeeVoteService employeeVoteService;

    public EmployeeVoteController() {
        this.employeeVoteService = new EmployeeVoteService();
    }

    public boolean addVote(int recommendationId, int userId, String voteType, String voteDate) {
        return employeeVoteService.addVote(recommendationId, userId, voteType, voteDate);
    }

    public List<EmployeeVote> getVotesByRecommendationId(int recommendationId) {
        return employeeVoteService.getVotesByRecommendationId(recommendationId);
    }
}
