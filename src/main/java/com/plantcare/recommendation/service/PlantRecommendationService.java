package com.plantcare.recommendation.service;

import com.plantcare.plant.dto.PlantResponse;
import com.plantcare.plant.entity.Plant;
import com.plantcare.plant.repository.PlantRepository;
import com.plantcare.plant.service.PlantService;
import com.plantcare.recommendation.dto.RecommendationRequest;
import com.plantcare.recommendation.dto.RecommendationResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlantRecommendationService {

    private final PlantRepository plantRepository;
    private final PlantService plantService;

    public PlantRecommendationService(PlantRepository plantRepository, PlantService plantService) {
        this.plantRepository = plantRepository;
        this.plantService = plantService;
    }

    public List<RecommendationResponse> getRecommendations(RecommendationRequest request) {
        List<Plant> plants = plantRepository.findAll().stream()
                .filter(Plant::isActive)
                .collect(Collectors.toList());

        List<RecommendationResponse> scoredList = new ArrayList<>();

        for (Plant plant : plants) {
            int score = 0;
            List<String> reasons = new ArrayList<>();

            // 1. Indoor/Outdoor match (+20)
            if (request.getIndoorOutdoor() != null && request.getIndoorOutdoor().equalsIgnoreCase(plant.getIndoorOutdoor())) {
                score += 20;
                reasons.add("Suitable for " + plant.getIndoorOutdoor().toLowerCase() + " placement");
            }

            // 2. Sunlight match (+30)
            if (request.getSunlight() != null && plant.getSunlightRequirement() != null &&
                plant.getSunlightRequirement().toLowerCase().contains(request.getSunlight().toLowerCase())) {
                score += 30;
                reasons.add("Matches " + request.getSunlight() + " sunlight requirement");
            }

            // 3. Maintenance level match (+15)
            if (request.getMaintenanceRequirement() != null && plant.getMaintenanceLevel() != null &&
                plant.getMaintenanceLevel().equalsIgnoreCase(request.getMaintenanceRequirement())) {
                score += 15;
                reasons.add("Low maintenance effort required");
            }

            // 4. Budget match (+20)
            if (request.getMaxPrice() != null && plant.getPurchasePrice().compareTo(request.getMaxPrice()) <= 0) {
                score += 20;
                reasons.add("Fits within customer budget");
            }

            // 5. Suitable location match (+15)
            if (request.getLocationType() != null && plant.getSuitableLocations() != null &&
                plant.getSuitableLocations().toLowerCase().contains(request.getLocationType().toLowerCase())) {
                score += 15;
                reasons.add("Ideal for corporate " + request.getLocationType().toLowerCase() + " environments");
            }

            if (score > 0) {
                PlantResponse pResp = plantService.getPlantById(plant.getId());
                scoredList.add(new RecommendationResponse(pResp, score, reasons));
            }
        }

        return scoredList.stream()
                .sorted(Comparator.comparingInt(RecommendationResponse::getScore).reversed())
                .limit(10)
                .collect(Collectors.toList());
    }
}
