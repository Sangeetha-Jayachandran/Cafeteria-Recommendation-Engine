package com.cafeteria.server.models;

import java.io.Serializable;

public class Recommendation implements Serializable {
    private int recommendationId;
    private int chefId;
    private int itemId;
    private String dateRecommended;

    public Recommendation() {}

    public Recommendation(int chefId, int itemId) {
        this.chefId = chefId;
        this.itemId = itemId;
    }

    public int getRecommendationId() {
        return recommendationId;
    }

    public void setRecommendationId(int recommendationId) {
        this.recommendationId = recommendationId;
    }

    public int getChefId() {
        return chefId;
    }

    public void setChefId(int chefId) {
        this.chefId = chefId;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getDateRecommended() {
        return dateRecommended;
    }

    public void setDateRecommended(String dateRecommended) {
        this.dateRecommended = dateRecommended;
    }
}
