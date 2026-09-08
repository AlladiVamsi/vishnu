package com.plantcare.report.service;

import com.plantcare.auth.repository.UserRepository;
import com.plantcare.booking.repository.BookingRepository;
import com.plantcare.company.repository.CompanyRepository;
import com.plantcare.plant.repository.PlantRepository;
import com.plantcare.plantinstance.repository.PlantInstanceRepository;
import com.plantcare.servicevisit.repository.ServiceVisitRepository;
import com.plantcare.worker.repository.WorkerProfileRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AdminReportService {

    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final PlantRepository plantRepository;
    private final BookingRepository bookingRepository;
    private final WorkerProfileRepository workerRepository;
    private final PlantInstanceRepository instanceRepository;
    private final ServiceVisitRepository visitRepository;

    public AdminReportService(UserRepository userRepository,
                              CompanyRepository companyRepository,
                              PlantRepository plantRepository,
                              BookingRepository bookingRepository,
                              WorkerProfileRepository workerRepository,
                              PlantInstanceRepository instanceRepository,
                              ServiceVisitRepository visitRepository) {
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
        this.plantRepository = plantRepository;
        this.bookingRepository = bookingRepository;
        this.workerRepository = workerRepository;
        this.instanceRepository = instanceRepository;
        this.visitRepository = visitRepository;
    }

    public Map<String, Object> getAdminDashboardMetrics() {
        Map<String, Object> metrics = new HashMap<>();

        metrics.put("totalUsers", userRepository.count());
        metrics.put("totalCompanies", companyRepository.count());
        metrics.put("totalPlantsInCatalog", plantRepository.count());
        metrics.put("totalBookingsPlaced", bookingRepository.count());
        metrics.put("totalActiveWorkers", workerRepository.count());
        metrics.put("totalPhysicalPlantsInstalled", instanceRepository.count());
        metrics.put("totalServiceVisitsScheduled", visitRepository.count());

        return metrics;
    }
}
