package com.plantcare.recommendation.dto;

import com.plantcare.plant.dto.PlantResponse;

import java.util.List;

public class RecommendationResponse {

    private PlantResponse plant;
    private int score;
    private List<String> reasons;

    public RecommendationResponse() {}

    public RecommendationResponse(PlantResponse plant, int score, List<String> reasons) {
        this.plant = plant;
        this.score = score;
        this.reasons = reasons;
    }

    public PlantResponse getPlant() { return plant; }
    public void setPlant(PlantResponse plant) { this.plant = plant; }

    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }

    public List<String> getReasons() { return reasons; }
    public void setReasons(List<String> reasons) { this.reasons = reasons; }
}
