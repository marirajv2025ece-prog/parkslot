package com.parkspot.repository;

import com.parkspot.entity.VisitorVehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VisitorVehicleRepository extends JpaRepository<VisitorVehicle, Long> {

    Optional<VisitorVehicle> findByVehicleNumber(String vehicleNumber);

    boolean existsByVehicleNumber(String vehicleNumber);
}
