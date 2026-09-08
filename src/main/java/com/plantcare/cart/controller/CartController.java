package com.plantcare.cart.controller;

import com.plantcare.cart.dto.AddToCartRequest;
import com.plantcare.cart.dto.CartResponse;
import com.plantcare.cart.service.CartService;
import com.plantcare.common.response.ApiResponse;
import com.plantcare.security.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/cart")
@Tag(name = "Cart & Pricing Engine", description = "Shopping Cart and Pricing Calculation REST APIs")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    @Operation(summary = "Get user cart with calculated subtotal, installation, tax, and totals")
    public ResponseEntity<ApiResponse<CartResponse>> getCart(
            @RequestParam(required = false) UUID userId,
            @RequestHeader(value = "X-User-Id", required = false) UUID headerUserId) {
        UUID effectiveUserId = resolveUserId(userId, headerUserId, null);
        CartResponse cart = cartService.getOrCreateCart(effectiveUserId);
        return ResponseEntity.ok(ApiResponse.success(cart));
    }

    @PostMapping("/items")
    @Operation(summary = "Add or update plant item in cart")
    public ResponseEntity<ApiResponse<CartResponse>> addItem(
            @Valid @RequestBody AddToCartRequest request,
            @RequestParam(required = false) UUID userId,
            @RequestHeader(value = "X-User-Id", required = false) UUID headerUserId) {
        UUID effectiveUserId = resolveUserId(userId, headerUserId, request.getUserId());
        CartResponse cart = cartService.addItemToCart(effectiveUserId, request);
        return ResponseEntity.ok(ApiResponse.success("Item added to cart", cart));
    }

    @DeleteMapping("/items/{id}")
    @Operation(summary = "Remove item from cart")
    public ResponseEntity<ApiResponse<Void>> removeItem(
            @PathVariable UUID id,
            @RequestParam(required = false) UUID userId,
            @RequestHeader(value = "X-User-Id", required = false) UUID headerUserId) {
        UUID effectiveUserId = resolveUserId(userId, headerUserId, null);
        cartService.removeItemFromCart(effectiveUserId, id);
        return ResponseEntity.ok(ApiResponse.success("Item removed from cart", null));
    }

    @DeleteMapping
    @Operation(summary = "Clear all cart items")
    public ResponseEntity<ApiResponse<Void>> clearCart(
            @RequestParam(required = false) UUID userId,
            @RequestHeader(value = "X-User-Id", required = false) UUID headerUserId) {
        UUID effectiveUserId = resolveUserId(userId, headerUserId, null);
        cartService.clearCart(effectiveUserId);
        return ResponseEntity.ok(ApiResponse.success("Cart cleared", null));
    }

    private UUID resolveUserId(UUID paramUserId, UUID headerUserId, UUID bodyUserId) {
        if (bodyUserId != null) return bodyUserId;
        if (paramUserId != null) return paramUserId;
        if (headerUserId != null) return headerUserId;
        UUID secId = SecurityUtils.getCurrentUserId();
        if (secId != null) return secId;
        throw new com.plantcare.common.exception.ValidationException("User ID is required (pass via userId param/body or X-User-Id header)");
    }
}
