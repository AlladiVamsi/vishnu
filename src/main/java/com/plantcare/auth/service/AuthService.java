package com.plantcare.auth.service;

import com.plantcare.auth.dto.*;
import com.plantcare.auth.entity.Role;
import com.plantcare.auth.entity.User;
import com.plantcare.auth.entity.UserStatus;
import com.plantcare.auth.repository.UserRepository;
import com.plantcare.common.exception.DuplicateResourceException;
import com.plantcare.common.exception.ResourceNotFoundException;
import com.plantcare.common.exception.UnauthorizedException;
import com.plantcare.company.entity.Company;
import com.plantcare.company.repository.CompanyRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository,
                       CompanyRepository companyRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("User with email '" + request.getEmail() + "' already exists");
        }
        if (request.getPhone() != null && userRepository.existsByPhone(request.getPhone())) {
            throw new DuplicateResourceException("User with phone '" + request.getPhone() + "' already exists");
        }

        Company company = null;
        if (request.getCompanyName() != null && !request.getCompanyName().isBlank()) {
            company = new Company();
            company.setCompanyName(request.getCompanyName());
            company.setRegistrationNumber(request.getRegistrationNumber());
            company.setEmail(request.getEmail());
            company.setPhone(request.getPhone());
            company.setContactPerson(request.getFirstName() + " " + (request.getLastName() != null ? request.getLastName() : ""));
            company = companyRepository.save(company);
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setRole(Role.CUSTOMER);
        user.setStatus(UserStatus.ACTIVE);
        user.setCompany(company);

        user = userRepository.save(user);

        return new AuthResponse(mapToUserResponse(user));
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmailOrPhone(request.getUsername(), request.getUsername())
                .orElseThrow(() -> new UnauthorizedException("Invalid email/phone or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new UnauthorizedException("Invalid email/phone or password");
        }

        return new AuthResponse(mapToUserResponse(user));
    }

    public UserResponse getCurrentUser(UUID userId) {
        if (userId == null) {
            throw new UnauthorizedException("No active user session");
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        return mapToUserResponse(user);
    }

    private UserResponse mapToUserResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setEmail(user.getEmail());
        response.setPhone(user.getPhone());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setRole(user.getRole());
        response.setStatus(user.getStatus());
        if (user.getCompany() != null) {
            response.setCompanyId(user.getCompany().getId());
            response.setCompanyName(user.getCompany().getCompanyName());
        }
        response.setCreatedAt(user.getCreatedAt());
        return response;
    }
}
