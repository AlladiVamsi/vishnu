package com.plantcare.plant.service;

import com.plantcare.common.exception.DuplicateResourceException;
import com.plantcare.common.exception.ResourceNotFoundException;
import com.plantcare.plant.dto.CategoryResponse;
import com.plantcare.plant.dto.CreateCategoryRequest;
import com.plantcare.plant.entity.PlantCategory;
import com.plantcare.plant.repository.PlantCategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private final PlantCategoryRepository categoryRepository;

    public CategoryService(PlantCategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    public CategoryResponse createCategory(CreateCategoryRequest request) {
        if (categoryRepository.existsByName(request.getName())) {
            throw new DuplicateResourceException("Plant category with name '" + request.getName() + "' already exists");
        }

        PlantCategory category = new PlantCategory();
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        category.setActive(request.isActive());

        category = categoryRepository.save(category);
        return mapToResponse(category);
    }

    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public CategoryResponse getCategoryById(UUID id) {
        PlantCategory category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PlantCategory", "id", id));
        return mapToResponse(category);
    }

    private CategoryResponse mapToResponse(PlantCategory category) {
        CategoryResponse response = new CategoryResponse();
        response.setId(category.getId());
        response.setName(category.getName());
        response.setDescription(category.getDescription());
        response.setActive(category.isActive());
        return response;
    }
}
