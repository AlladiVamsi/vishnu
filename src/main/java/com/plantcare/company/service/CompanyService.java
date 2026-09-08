package com.plantcare.company.service;

import com.plantcare.common.exception.ResourceNotFoundException;
import com.plantcare.company.dto.CompanyLocationResponse;
import com.plantcare.company.dto.CreateCompanyLocationRequest;
import com.plantcare.company.entity.Company;
import com.plantcare.company.entity.CompanyLocation;
import com.plantcare.company.repository.CompanyLocationRepository;
import com.plantcare.company.repository.CompanyRepository;
import com.plantcare.security.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final CompanyLocationRepository locationRepository;

    public CompanyService(CompanyRepository companyRepository, CompanyLocationRepository locationRepository) {
        this.companyRepository = companyRepository;
        this.locationRepository = locationRepository;
    }

    @Transactional
    public CompanyLocationResponse createLocation(CreateCompanyLocationRequest request) {
        SecurityUtils.verifyCompanyOwnership(request.getCompanyId());

        Company company = companyRepository.findById(request.getCompanyId())
                .orElseThrow(() -> new ResourceNotFoundException("Company", "id", request.getCompanyId()));

        CompanyLocation location = new CompanyLocation();
        location.setCompany(company);
        location.setLocationName(request.getLocationName());
        location.setAddress(request.getAddress());
        location.setCity(request.getCity());
        location.setState(request.getState());
        location.setCountry(request.getCountry());
        location.setPincode(request.getPincode());
        location.setLatitude(request.getLatitude());
        location.setLongitude(request.getLongitude());
        if (request.getLocationType() != null) location.setLocationType(request.getLocationType());
        if (request.getIndoorOutdoor() != null) location.setIndoorOutdoor(request.getIndoorOutdoor());
        location.setApproximateArea(request.getApproximateArea());
        location.setNotes(request.getNotes());

        location = locationRepository.save(location);
        return mapToResponse(location);
    }

    public List<CompanyLocationResponse> getCompanyLocations(UUID companyId) {
        SecurityUtils.verifyCompanyOwnership(companyId);
        return locationRepository.findByCompanyId(companyId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public CompanyLocationResponse getLocationById(UUID locationId) {
        CompanyLocation location = locationRepository.findById(locationId)
                .orElseThrow(() -> new ResourceNotFoundException("CompanyLocation", "id", locationId));
        SecurityUtils.verifyCompanyOwnership(location.getCompany().getId());
        return mapToResponse(location);
    }

    private CompanyLocationResponse mapToResponse(CompanyLocation location) {
        CompanyLocationResponse response = new CompanyLocationResponse();
        response.setId(location.getId());
        response.setCompanyId(location.getCompany().getId());
        response.setLocationName(location.getLocationName());
        response.setAddress(location.getAddress());
        response.setCity(location.getCity());
        response.setState(location.getState());
        response.setCountry(location.getCountry());
        response.setPincode(location.getPincode());
        response.setLatitude(location.getLatitude());
        response.setLongitude(location.getLongitude());
        response.setLocationType(location.getLocationType());
        response.setIndoorOutdoor(location.getIndoorOutdoor());
        response.setApproximateArea(location.getApproximateArea());
        response.setNotes(location.getNotes());
        response.setCreatedAt(location.getCreatedAt());
        return response;
    }
}
