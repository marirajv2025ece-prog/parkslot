package com.parkspot.dto;

import com.parkspot.entity.VisitorVehicle;

public record VehicleResponse(Long id, String vehicleNumber, String vehicleType,
                              String visitorName, String mobileNumber) {

    public static VehicleResponse from(VisitorVehicle vehicle) {
        return new VehicleResponse(vehicle.getId(), vehicle.getVehicleNumber(), vehicle.getVehicleType(),
                vehicle.getVisitorName(), vehicle.getMobileNumber());
    }
}
