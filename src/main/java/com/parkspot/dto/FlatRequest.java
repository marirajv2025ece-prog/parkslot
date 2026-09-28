package com.parkspot.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record FlatRequest(

        @NotBlank(message = "Flat number is required")
        @Pattern(regexp = ValidationPatterns.FLAT_NUMBER,
                message = "Flat number may contain only letters, digits and '-' (max 10 characters)")
        String flatNumber,

        @NotBlank(message = "Resident name is required")
        @Size(min = 2, max = 50, message = "Resident name must be 2 to 50 characters")
        String residentName) {
}
