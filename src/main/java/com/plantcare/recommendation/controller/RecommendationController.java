package com.plantcare.recommendation.controller;

import com.plantcare.common.response.ApiResponse;
import com.plantcare.recommendation.dto.RecommendationRequest;
import com.plantcare.recommendation.dto.RecommendationResponse;
import com.plantcare.recommendation.service.PlantRecommendationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/recommendations")
@Tag(name = "Plant Recommendation Engine", description = "Rule-Based Plant Recommendation REST APIs")
public class RecommendationController {

    private final PlantRecommendationService recommendationService;

    public RecommendationController(PlantRecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @PostMapping
    @Operation(summary = "Get rule-scored plant recommendations based on office space, sunlight, budget, and location")
    public ResponseEntity<ApiResponse<List<RecommendationResponse>>> getRecommendations(@RequestBody RecommendationRequest request) {
        List<RecommendationResponse> recommendations = recommendationService.getRecommendations(request);
        return ResponseEntity.ok(ApiResponse.success(recommendations));
    }
}
