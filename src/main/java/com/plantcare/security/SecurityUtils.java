package com.plantcare.security;

import com.plantcare.common.exception.ForbiddenException;
import com.plantcare.common.exception.UnauthorizedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.UUID;

public class SecurityUtils {

    public static CustomUserDetails getCurrentUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && (authentication.getPrincipal() instanceof CustomUserDetails)) {
            return (CustomUserDetails) authentication.getPrincipal();
        }
        return null;
    }

    public static UUID getCurrentUserId() {
        CustomUserDetails userDetails = getCurrentUserDetails();
        return userDetails != null ? userDetails.getId() : null;
    }

    public static UUID getCurrentUserCompanyId() {
        CustomUserDetails userDetails = getCurrentUserDetails();
        return userDetails != null ? userDetails.getCompanyId() : null;
    }

    public static boolean hasRole(String role) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) return true; // Allow all in simplified auth mode
        return authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_" + role));
    }

    public static void verifyCompanyOwnership(UUID requestedCompanyId) {
        // Simplified authorization verification
        CustomUserDetails userDetails = getCurrentUserDetails();
        if (userDetails != null && userDetails.getCompanyId() != null) {
            if (!userDetails.getCompanyId().equals(requestedCompanyId)) {
                throw new ForbiddenException("Access denied: You cannot access resources belonging to another company");
            }
        }
    }
}
