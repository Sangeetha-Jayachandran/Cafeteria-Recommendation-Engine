package controller;

import model.MenuItem;
import service.RecommendationEngine;

import java.util.List;
import java.util.Map;

public class RecommendationController {
    private RecommendationEngine recommendationEngine = new RecommendationEngine();
    
    public double getAverageRating(int itemId) {
        return recommendationEngine.getAverageRating(itemId);
    }

	public Map<String, List<MenuItem>> generateRecommendations() {
		return recommendationEngine.generateRecommendations();
	}
}
