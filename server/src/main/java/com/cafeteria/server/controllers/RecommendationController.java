package com.cafeteria.server.controllers;

import com.cafeteria.server.models.MenuItem;
import com.cafeteria.server.services.RecommendationService;

import java.util.List;

public class RecommendationController {
    private final RecommendationService recommendationService;

    public RecommendationController() {
        this.recommendationService = new RecommendationService();
    }

    public void addRecommendation(List<Integer> itemIds, int chefId) {
        recommendationService.addRecommendation(itemIds, chefId);
    }

    public String viewRecommendations(Integer userId) {
        return recommendationService.viewRecommendations(userId);
    }


    public List<MenuItem> getTopItems(String category) {
        return recommendationService.getTopRecommendation(category);
    }

    public List<MenuItem> getLatestRecommendations() {
        return recommendationService.getLatestRecommendations();
    }

}
