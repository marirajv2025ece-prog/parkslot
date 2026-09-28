package com.parkspot.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record VehicleRequest(

        @NotBlank(message = "Vehicle number is required")
        @Pattern(regexp = ValidationPatterns.VEHICLE_NUMBER,
                message = "Vehicle number is invalid (example: TN38AB1234)")
        String vehicleNumber,

        @NotBlank(message = "Vehicle type is required")
        @Size(max = 20, message = "Vehicle type must be at most 20 characters")
        String vehicleType,

        @NotBlank(message = "Visitor name is required")
        @Size(min = 2, max = 50, message = "Visitor name must be 2 to 50 characters")
        String visitorName,

        @NotBlank(message = "Mobile number is required")
        @Pattern(regexp = ValidationPatterns.MOBILE_NUMBER,
                message = "Mobile number must be 10 digits and start with 6, 7, 8 or 9")
        String mobileNumber) {
}
