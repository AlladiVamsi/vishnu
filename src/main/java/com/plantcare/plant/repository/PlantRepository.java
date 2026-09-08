package com.plantcare.plant.repository;

import com.plantcare.plant.entity.Plant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.UUID;

@Repository
public interface PlantRepository extends JpaRepository<Plant, UUID>, JpaSpecificationExecutor<Plant> {

    // PlantRepository inherits JpaSpecificationExecutor for dynamic type-safe searching

}
