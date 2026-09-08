package com.plantcare.plant.dto;

import java.util.UUID;

public class PlantImageResponse {

    private UUID id;
    private String imageUrl;
    private boolean primaryImage;

    public PlantImageResponse() {}

    public PlantImageResponse(UUID id, String imageUrl, boolean primaryImage) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.primaryImage = primaryImage;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public boolean isPrimaryImage() { return primaryImage; }
    public void setPrimaryImage(boolean primaryImage) { this.primaryImage = primaryImage; }
}
