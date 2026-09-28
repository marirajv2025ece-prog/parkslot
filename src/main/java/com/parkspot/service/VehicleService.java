package com.parkspot.service;

import com.parkspot.dto.ParkingEntryRequest;
import com.parkspot.dto.VehicleRequest;
import com.parkspot.dto.VehicleResponse;
import com.parkspot.entity.VisitorVehicle;
import com.parkspot.exception.DuplicateResourceException;
import com.parkspot.exception.InvalidParkingOperationException;
import com.parkspot.exception.ResourceNotFoundException;
import com.parkspot.repository.ParkingRecordRepository;
import com.parkspot.repository.VisitorVehicleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class VehicleService {

    private static final String DEFAULT_VEHICLE_TYPE = "Other";

    private final VisitorVehicleRepository vehicleRepository;
    private final ParkingRecordRepository parkingRecordRepository;

    public VehicleService(VisitorVehicleRepository vehicleRepository,
                          ParkingRecordRepository parkingRecordRepository) {
        this.vehicleRepository = vehicleRepository;
        this.parkingRecordRepository = parkingRecordRepository;
    }

    /** "tn 38 ab 1234" -> "TN38AB1234" (so the same vehicle is always stored the same way). */
    public static String normalizeNumber(String vehicleNumber) {
        return vehicleNumber.replaceAll("\\s+", "").toUpperCase();
    }

    @Transactional
    public VehicleResponse createVehicle(VehicleRequest request) {
        String number = normalizeNumber(request.vehicleNumber());
        if (vehicleRepository.existsByVehicleNumber(number)) {
            throw new DuplicateResourceException("Vehicle " + number + " is already registered.");
        }
        VisitorVehicle vehicle = new VisitorVehicle();
        copyDetails(request, vehicle);
        return VehicleResponse.from(vehicleRepository.save(vehicle));
    }

    public List<VehicleResponse> getAllVehicles() {
        return vehicleRepository.findAll().stream().map(VehicleResponse::from).toList();
    }

    public VehicleResponse getVehicleById(Long id) {
        return VehicleResponse.from(findEntityById(id));
    }

    @Transactional
    public VehicleResponse updateVehicle(Long id, VehicleRequest request) {
        VisitorVehicle vehicle = findEntityById(id);
        String newNumber = normalizeNumber(request.vehicleNumber());
        boolean numberChanged = !vehicle.getVehicleNumber().equals(newNumber);
        if (numberChanged && vehicleRepository.existsByVehicleNumber(newNumber)) {
            throw new DuplicateResourceException("Vehicle " + newNumber + " is already registered.");
        }
        copyDetails(request, vehicle);
        return VehicleResponse.from(vehicleRepository.save(vehicle));
    }

    @Transactional
    public void deleteVehicle(Long id) {
        VisitorVehicle vehicle = findEntityById(id);
        // a vehicle that has parking history must be kept, otherwise the log would lose data
        if (parkingRecordRepository.existsByVisitorVehicleId(id)) {
            throw new InvalidParkingOperationException(
                    "Vehicle " + vehicle.getVehicleNumber() + " has parking records and cannot be deleted.");
        }
        vehicleRepository.delete(vehicle);
    }

    /**
     * Used by ParkingService. Returns the vehicle if it is already known.
     * If it is new, it is registered automatically from the visitor details in the entry request.
     */
    @Transactional
    public VisitorVehicle findOrRegister(ParkingEntryRequest request) {
        String number = normalizeNumber(request.vehicleNumber());
        return vehicleRepository.findByVehicleNumber(number)
                .orElseGet(() -> registerNewVehicle(number, request));
    }

    private VisitorVehicle registerNewVehicle(String number, ParkingEntryRequest request) {
        if (isBlank(request.visitorName()) || isBlank(request.mobileNumber())) {
            throw new ResourceNotFoundException("Vehicle " + number + " is not registered. "
                    + "Enter visitor name and mobile number, or create the vehicle first (POST /api/vehicles).");
        }
        VisitorVehicle vehicle = new VisitorVehicle();
        vehicle.setVehicleNumber(number);
        vehicle.setVisitorName(request.visitorName().trim());
        vehicle.setMobileNumber(request.mobileNumber().trim());
        vehicle.setVehicleType(isBlank(request.vehicleType()) ? DEFAULT_VEHICLE_TYPE : request.vehicleType().trim());
        return vehicleRepository.save(vehicle);
    }

    private VisitorVehicle findEntityById(Long id) {
        return vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle with id " + id + " not found."));
    }

    private void copyDetails(VehicleRequest request, VisitorVehicle vehicle) {
        vehicle.setVehicleNumber(normalizeNumber(request.vehicleNumber()));
        vehicle.setVehicleType(request.vehicleType().trim());
        vehicle.setVisitorName(request.visitorName().trim());
        vehicle.setMobileNumber(request.mobileNumber().trim());
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
