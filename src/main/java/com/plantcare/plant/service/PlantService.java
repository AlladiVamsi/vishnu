package com.plantcare.plant.service;

import com.plantcare.common.exception.ResourceNotFoundException;
import com.plantcare.common.response.PageResponse;
import com.plantcare.inventory.entity.Inventory;
import com.plantcare.inventory.repository.InventoryRepository;
import com.plantcare.inventory.service.InventoryService;
import com.plantcare.plant.dto.CreatePlantRequest;
import com.plantcare.plant.dto.PlantImageResponse;
import com.plantcare.plant.dto.PlantResponse;
import com.plantcare.plant.entity.Plant;
import com.plantcare.plant.entity.PlantCategory;
import com.plantcare.plant.entity.PlantImage;
import com.plantcare.plant.repository.PlantCategoryRepository;
import com.plantcare.plant.repository.PlantImageRepository;
import com.plantcare.plant.repository.PlantRepository;
import com.plantcare.storage.FileStorageService;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PlantService {

    private final PlantRepository plantRepository;
    private final PlantCategoryRepository categoryRepository;
    private final PlantImageRepository imageRepository;
    private final InventoryRepository inventoryRepository;
    private final InventoryService inventoryService;
    private final FileStorageService fileStorageService;

    public PlantService(PlantRepository plantRepository,
                        PlantCategoryRepository categoryRepository,
                        PlantImageRepository imageRepository,
                        InventoryRepository inventoryRepository,
                        InventoryService inventoryService,
                        FileStorageService fileStorageService) {
        this.plantRepository = plantRepository;
        this.categoryRepository = categoryRepository;
        this.imageRepository = imageRepository;
        this.inventoryRepository = inventoryRepository;
        this.inventoryService = inventoryService;
        this.fileStorageService = fileStorageService;
    }

    @Transactional
    public PlantResponse createPlant(CreatePlantRequest request) {
        PlantCategory category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("PlantCategory", "id", request.getCategoryId()));

        Plant plant = new Plant();
        plant.setCategory(category);
        plant.setName(request.getName());
        plant.setScientificName(request.getScientificName());
        plant.setDescription(request.getDescription());
        plant.setPurchasePrice(request.getPurchasePrice());
        plant.setRentalPrice(request.getRentalPrice());
        if (request.getInstallationCharge() != null) plant.setInstallationCharge(request.getInstallationCharge());
        plant.setSize(request.getSize());
        plant.setHeight(request.getHeight());
        if (request.getIndoorOutdoor() != null) plant.setIndoorOutdoor(request.getIndoorOutdoor());
        plant.setSunlightRequirement(request.getSunlightRequirement());
        plant.setWaterRequirement(request.getWaterRequirement());
        plant.setTemperatureRequirement(request.getTemperatureRequirement());
        plant.setHumidityRequirement(request.getHumidityRequirement());
        plant.setMaintenanceLevel(request.getMaintenanceLevel());
        plant.setMaintenanceFrequency(request.getMaintenanceFrequency());
        plant.setSuitableLocations(request.getSuitableLocations());
        plant.setCareInstructions(request.getCareInstructions());
        plant.setSaleAvailable(request.isSaleAvailable());
        plant.setRentalAvailable(request.isRentalAvailable());
        plant.setActive(true);

        plant = plantRepository.save(plant);

        inventoryService.createInventoryForPlant(plant, request.getInitialInventory());

        return mapToResponse(plant);
    }

    public PageResponse<PlantResponse> searchPlants(UUID categoryId, String indoorOutdoor, BigDecimal minPrice, BigDecimal maxPrice, String search, int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Specification<Plant> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("active"), true));

            if (categoryId != null) {
                predicates.add(cb.equal(root.get("category").get("id"), categoryId));
            }
            if (indoorOutdoor != null && !indoorOutdoor.isBlank()) {
                predicates.add(cb.equal(root.get("indoorOutdoor"), indoorOutdoor));
            }
            if (minPrice != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("purchasePrice"), minPrice));
            }
            if (maxPrice != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("purchasePrice"), maxPrice));
            }
            if (search != null && !search.isBlank()) {
                String pattern = "%" + search.toLowerCase() + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("name")), pattern),
                        cb.like(cb.lower(root.get("scientificName")), pattern)
                ));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Page<Plant> plantPage = plantRepository.findAll(spec, pageable);
        Page<PlantResponse> responsePage = plantPage.map(this::mapToResponse);

        return PageResponse.from(responsePage);
    }

    public PlantResponse getPlantById(UUID id) {
        Plant plant = plantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Plant", "id", id));
        return mapToResponse(plant);
    }

    @Transactional
    public PlantImageResponse uploadPlantImage(UUID plantId, MultipartFile file, boolean primaryImage) {
        Plant plant = plantRepository.findById(plantId)
                .orElseThrow(() -> new ResourceNotFoundException("Plant", "id", plantId));

        String storageKey = fileStorageService.upload(file, "plants");
        String imageUrl = fileStorageService.getUrl(storageKey);

        PlantImage image = new PlantImage();
        image.setPlant(plant);
        image.setStorageKey(storageKey);
        image.setImageUrl(imageUrl);
        image.setContentType(file.getContentType());
        image.setFileSize(file.getSize());
        image.setPrimaryImage(primaryImage);

        image = imageRepository.save(image);
        return new PlantImageResponse(image.getId(), image.getImageUrl(), image.isPrimaryImage());
    }

    private PlantResponse mapToResponse(Plant plant) {
        PlantResponse response = new PlantResponse();
        response.setId(plant.getId());
        response.setCategoryId(plant.getCategory().getId());
        response.setCategoryName(plant.getCategory().getName());
        response.setName(plant.getName());
        response.setScientificName(plant.getScientificName());
        response.setDescription(plant.getDescription());
        response.setPurchasePrice(plant.getPurchasePrice());
        response.setRentalPrice(plant.getRentalPrice());
        response.setInstallationCharge(plant.getInstallationCharge());
        response.setSize(plant.getSize());
        response.setHeight(plant.getHeight());
        response.setIndoorOutdoor(plant.getIndoorOutdoor());
        response.setSunlightRequirement(plant.getSunlightRequirement());
        response.setWaterRequirement(plant.getWaterRequirement());
        response.setTemperatureRequirement(plant.getTemperatureRequirement());
        response.setHumidityRequirement(plant.getHumidityRequirement());
        response.setMaintenanceLevel(plant.getMaintenanceLevel());
        response.setMaintenanceFrequency(plant.getMaintenanceFrequency());
        response.setSuitableLocations(plant.getSuitableLocations());
        response.setCareInstructions(plant.getCareInstructions());
        response.setSaleAvailable(plant.isSaleAvailable());
        response.setRentalAvailable(plant.isRentalAvailable());
        response.setActive(plant.isActive());
        response.setCreatedAt(plant.getCreatedAt());

        List<PlantImageResponse> imgResponses = plant.getImages().stream()
                .map(img -> new PlantImageResponse(img.getId(), img.getImageUrl(), img.isPrimaryImage()))
                .collect(Collectors.toList());
        response.setImages(imgResponses);

        inventoryRepository.findByPlantId(plant.getId())
                .ifPresent(inv -> response.setAvailableQuantity(inv.getAvailableQuantity()));

        return response;
    }
}
