package com.plantcare.greenoffice.service;

import com.plantcare.plantinstance.entity.PlantInstance;
import com.plantcare.plantinstance.repository.PlantInstanceRepository;
import com.plantcare.security.SecurityUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class GreenOfficeScoreService {

    private final PlantInstanceRepository instanceRepository;

    public GreenOfficeScoreService(PlantInstanceRepository instanceRepository) {
        this.instanceRepository = instanceRepository;
    }

    public Map<String, Object> calculateGreenOfficeScore(UUID companyId) {
        SecurityUtils.verifyCompanyOwnership(companyId);

        List<PlantInstance> instances = instanceRepository.findByCompanyId(companyId);

        int totalPlants = instances.size();
        if (totalPlants == 0) {
            Map<String, Object> emptyResult = new HashMap<>();
            emptyResult.put("score", 0);
            emptyResult.put("rating", "NEEDS_GREENERY");
            emptyResult.put("totalPlants", 0);
            emptyResult.put("healthyPlants", 0);
            emptyResult.put("recommendation", "Start installing indoor plants to boost corporate air quality and employee wellness.");
            return emptyResult;
        }

        long healthyCount = instances.stream()
                .filter(i -> "HEALTHY".equalsIgnoreCase(i.getCurrentHealthStatus()))
                .count();

        long uniqueSpeciesCount = instances.stream()
                .map(i -> i.getPlant().getId())
                .distinct()
                .count();

        // Score components (0-100 max):
        // 1. Plant Quantity Score (up to 30 pts): 1 pt per plant up to 30
        double quantityScore = Math.min(30.0, totalPlants);

        // 2. Plant Health Score (up to 40 pts): (healthy / total) * 40
        double healthScore = ((double) healthyCount / totalPlants) * 40.0;

        // 3. Plant Diversity Score (up to 30 pts): 5 pts per unique species up to 30
        double diversityScore = Math.min(30.0, uniqueSpeciesCount * 5.0);

        int finalScore = (int) Math.round(quantityScore + healthScore + diversityScore);
        finalScore = Math.min(100, Math.max(0, finalScore));

        String rating;
        if (finalScore >= 85) rating = "PLATINUM_GREEN_OFFICE";
        else if (finalScore >= 70) rating = "GOLD_GREEN_OFFICE";
        else if (finalScore >= 50) rating = "SILVER_GREEN_OFFICE";
        else rating = "BRONZE_GREEN_OFFICE";

        Map<String, Object> result = new HashMap<>();
        result.put("companyId", companyId);
        result.put("score", finalScore);
        result.put("rating", rating);
        result.put("totalPlants", totalPlants);
        result.put("healthyPlants", healthyCount);
        result.put("uniqueSpeciesCount", uniqueSpeciesCount);
        result.put("healthPercentage", Math.round(((double) healthyCount / totalPlants) * 100.0));

        return result;
    }
}
