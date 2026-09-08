package com.plantcare.cart.service;

import com.plantcare.auth.entity.User;
import com.plantcare.auth.repository.UserRepository;
import com.plantcare.cart.dto.AddToCartRequest;
import com.plantcare.cart.dto.CartItemResponse;
import com.plantcare.cart.dto.CartResponse;
import com.plantcare.cart.entity.Cart;
import com.plantcare.cart.entity.CartItem;
import com.plantcare.cart.repository.CartItemRepository;
import com.plantcare.cart.repository.CartRepository;
import com.plantcare.common.exception.ResourceNotFoundException;
import com.plantcare.maintenance.entity.MaintenancePlan;
import com.plantcare.maintenance.repository.MaintenancePlanRepository;
import com.plantcare.plant.entity.Plant;
import com.plantcare.plant.repository.PlantRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CartService {

    private static final BigDecimal TAX_RATE = new BigDecimal("0.18"); // 18% Tax

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final PlantRepository plantRepository;
    private final MaintenancePlanRepository maintenancePlanRepository;

    public CartService(CartRepository cartRepository,
                       CartItemRepository cartItemRepository,
                       UserRepository userRepository,
                       PlantRepository plantRepository,
                       MaintenancePlanRepository maintenancePlanRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.userRepository = userRepository;
        this.plantRepository = plantRepository;
        this.maintenancePlanRepository = maintenancePlanRepository;
    }

    @Transactional
    public CartResponse getOrCreateCart(UUID userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    User user = userRepository.findById(userId)
                            .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    newCart.setCompany(user.getCompany());
                    return cartRepository.save(newCart);
                });
        return calculateCartTotals(cart);
    }

    @Transactional
    public CartResponse addItemToCart(UUID userId, AddToCartRequest request) {
        CartResponse cartResponse = getOrCreateCart(userId);
        Cart cart = cartRepository.findById(cartResponse.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        Plant plant = plantRepository.findById(request.getPlantId())
                .orElseThrow(() -> new ResourceNotFoundException("Plant", "id", request.getPlantId()));

        MaintenancePlan maintenancePlan = null;
        if (request.getMaintenancePlanId() != null) {
            maintenancePlan = maintenancePlanRepository.findById(request.getMaintenancePlanId())
                    .orElseThrow(() -> new ResourceNotFoundException("MaintenancePlan", "id", request.getMaintenancePlanId()));
        }

        BigDecimal unitPrice = "RENTAL".equalsIgnoreCase(request.getOrderType()) ? plant.getRentalPrice() : plant.getPurchasePrice();

        CartItem existingItem = cart.getItems().stream()
                .filter(item -> item.getPlant().getId().equals(plant.getId()) && item.getOrderType().equalsIgnoreCase(request.getOrderType()))
                .findFirst()
                .orElse(null);

        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + request.getQuantity());
            if (maintenancePlan != null) existingItem.setMaintenancePlan(maintenancePlan);
            cartItemRepository.save(existingItem);
        } else {
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setPlant(plant);
            newItem.setQuantity(request.getQuantity());
            newItem.setOrderType(request.getOrderType() != null ? request.getOrderType() : "PURCHASE");
            newItem.setUnitPrice(unitPrice);
            newItem.setInstallationCharge(plant.getInstallationCharge());
            newItem.setMaintenancePlan(maintenancePlan);

            cart.getItems().add(newItem);
            cartItemRepository.save(newItem);
        }

        return calculateCartTotals(cart);
    }

    @Transactional
    public void removeItemFromCart(UUID userId, UUID itemId) {
        CartResponse cartResponse = getOrCreateCart(userId);
        Cart cart = cartRepository.findById(cartResponse.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        cart.getItems().removeIf(item -> item.getId().equals(itemId));
        cartRepository.save(cart);
    }

    @Transactional
    public void clearCart(UUID userId) {
        CartResponse cartResponse = getOrCreateCart(userId);
        Cart cart = cartRepository.findById(cartResponse.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        cart.getItems().clear();
        cartRepository.save(cart);
    }

    public CartResponse calculateCartTotals(Cart cart) {
        BigDecimal subtotal = BigDecimal.ZERO;
        BigDecimal installationCharges = BigDecimal.ZERO;
        BigDecimal maintenanceCharges = BigDecimal.ZERO;

        List<CartItemResponse> itemResponses = new ArrayList<>();

        for (CartItem item : cart.getItems()) {
            BigDecimal itemSub = item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
            BigDecimal itemInstall = item.getInstallationCharge().multiply(BigDecimal.valueOf(item.getQuantity()));

            subtotal = subtotal.add(itemSub);
            installationCharges = installationCharges.add(itemInstall);

            CartItemResponse itemResp = new CartItemResponse();
            itemResp.setId(item.getId());
            itemResp.setPlantId(item.getPlant().getId());
            itemResp.setPlantName(item.getPlant().getName());
            if (!item.getPlant().getImages().isEmpty()) {
                itemResp.setPlantImageUrl(item.getPlant().getImages().get(0).getImageUrl());
            }
            itemResp.setQuantity(item.getQuantity());
            itemResp.setOrderType(item.getOrderType());
            itemResp.setUnitPrice(item.getUnitPrice());
            itemResp.setInstallationCharge(item.getInstallationCharge());
            itemResp.setItemSubtotal(itemSub);

            if (item.getMaintenancePlan() != null) {
                itemResp.setMaintenancePlanId(item.getMaintenancePlan().getId());
                itemResp.setMaintenancePlanName(item.getMaintenancePlan().getName());
                itemResp.setMaintenancePrice(item.getMaintenancePlan().getPrice());
                maintenanceCharges = maintenanceCharges.add(item.getMaintenancePlan().getPrice());
            }

            itemResponses.add(itemResp);
        }

        BigDecimal taxableAmount = subtotal.add(installationCharges).add(maintenanceCharges);
        BigDecimal tax = taxableAmount.multiply(TAX_RATE).setScale(2, RoundingMode.HALF_UP);
        BigDecimal discount = BigDecimal.ZERO;
        BigDecimal totalAmount = taxableAmount.add(tax).subtract(discount).setScale(2, RoundingMode.HALF_UP);

        CartResponse response = new CartResponse();
        response.setId(cart.getId());
        response.setItems(itemResponses);
        response.setSubtotal(subtotal);
        response.setInstallationCharges(installationCharges);
        response.setMaintenanceCharges(maintenanceCharges);
        response.setTax(tax);
        response.setDiscount(discount);
        response.setTotalAmount(totalAmount);

        return response;
    }
}
