package com.parkspot.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Body of POST /api/parking/entry.
 * vehicleNumber, flatNumber and slotNumber are mandatory.
 * visitorName, mobileNumber and vehicleType are needed only when the vehicle is new
 * (the vehicle is then registered automatically).
 */
public record ParkingEntryRequest(

        @NotBlank(message = "Vehicle number is required")
        @Pattern(regexp = ValidationPatterns.VEHICLE_NUMBER,
                message = "Vehicle number is invalid (example: TN38AB1234)")
        String vehicleNumber,

        @NotBlank(message = "Flat number is required")
        @Size(max = 10, message = "Flat number must be at most 10 characters")
        String flatNumber,

        @NotBlank(message = "Parking slot is required")
        @Size(max = 10, message = "Parking slot must be at most 10 characters")
        String slotNumber,

        @Size(max = 50, message = "Visitor name must be at most 50 characters")
        String visitorName,

        @Pattern(regexp = ValidationPatterns.MOBILE_NUMBER,
                message = "Mobile number must be 10 digits and start with 6, 7, 8 or 9")
        String mobileNumber,

        @Size(max = 20, message = "Vehicle type must be at most 20 characters")
        String vehicleType,

        @Size(max = 100, message = "Purpose must be at most 100 characters")
        String purpose) {
}
