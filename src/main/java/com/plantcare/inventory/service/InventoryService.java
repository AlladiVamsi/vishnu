package com.plantcare.inventory.service;

import com.plantcare.common.exception.InsufficientInventoryException;
import com.plantcare.common.exception.ResourceNotFoundException;
import com.plantcare.inventory.entity.Inventory;
import com.plantcare.inventory.repository.InventoryRepository;
import com.plantcare.plant.entity.Plant;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    @Transactional
    public Inventory createInventoryForPlant(Plant plant, int initialQuantity) {
        Inventory inventory = new Inventory();
        inventory.setPlant(plant);
        inventory.setTotalQuantity(initialQuantity);
        inventory.setAvailableQuantity(initialQuantity);
        inventory.setReservedQuantity(0);
        return inventoryRepository.save(inventory);
    }

    public Inventory getInventoryByPlantId(UUID plantId) {
        return inventoryRepository.findByPlantId(plantId)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory for plant", "plantId", plantId));
    }

    @Transactional
    public void reserveStock(UUID plantId, int quantity) {
        Inventory inventory = inventoryRepository.findByPlantIdWithPessimisticLock(plantId)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory for plant", "plantId", plantId));

        if (inventory.getAvailableQuantity() < quantity) {
            throw new InsufficientInventoryException(
                    String.format("Insufficient stock for plant '%s'. Requested: %d, Available: %d",
                            inventory.getPlant().getName(), quantity, inventory.getAvailableQuantity())
            );
        }

        inventory.setAvailableQuantity(inventory.getAvailableQuantity() - quantity);
        inventory.setReservedQuantity(inventory.getReservedQuantity() + quantity);
        inventoryRepository.save(inventory);
    }

    @Transactional
    public void releaseStock(UUID plantId, int quantity) {
        Inventory inventory = inventoryRepository.findByPlantIdWithPessimisticLock(plantId)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory for plant", "plantId", plantId));

        int releaseQty = Math.min(inventory.getReservedQuantity(), quantity);
        inventory.setReservedQuantity(inventory.getReservedQuantity() - releaseQty);
        inventory.setAvailableQuantity(inventory.getAvailableQuantity() + releaseQty);
        inventoryRepository.save(inventory);
    }

    @Transactional
    public void confirmInstallationStock(UUID plantId, int quantity, boolean isRental) {
        Inventory inventory = inventoryRepository.findByPlantIdWithPessimisticLock(plantId)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory for plant", "plantId", plantId));

        int confirmQty = Math.min(inventory.getReservedQuantity(), quantity);
        inventory.setReservedQuantity(inventory.getReservedQuantity() - confirmQty);
        if (isRental) {
            inventory.setRentalQuantity(inventory.getRentalQuantity() + confirmQty);
        } else {
            inventory.setInstalledQuantity(inventory.getInstalledQuantity() + confirmQty);
        }
        inventoryRepository.save(inventory);
    }
}
