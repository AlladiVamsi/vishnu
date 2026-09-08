package com.plantcare.security;

import com.plantcare.auth.entity.Role;
import com.plantcare.auth.entity.User;
import com.plantcare.common.exception.ForbiddenException;
import com.plantcare.company.entity.Company;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CompanyOwnershipSecurityTest {

    private UUID companyAId;
    private UUID companyBId;

    @BeforeEach
    void setUp() {
        companyAId = UUID.randomUUID();
        companyBId = UUID.randomUUID();

        Company companyA = new Company();
        companyA.setId(companyAId);

        User userA = new User();
        userA.setId(UUID.randomUUID());
        userA.setEmail("userA@companyA.com");
        userA.setRole(Role.CUSTOMER);
        userA.setCompany(companyA);

        CustomUserDetails userDetailsA = new CustomUserDetails(userA);
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetailsA, null, userDetailsA.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void verifyCompanyOwnership_OwnCompany_Success() {
        assertDoesNotThrow(() -> SecurityUtils.verifyCompanyOwnership(companyAId));
    }

    @Test
    void verifyCompanyOwnership_OtherCompany_ThrowsForbiddenException() {
        assertThrows(ForbiddenException.class, () -> SecurityUtils.verifyCompanyOwnership(companyBId));
    }
}
