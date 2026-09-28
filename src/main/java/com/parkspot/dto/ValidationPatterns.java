package com.parkspot.dto;

/** Regular expressions shared by the request DTOs (so they are written only once). */
public final class ValidationPatterns {

    // Examples: TN38AB1234, TN9AB1234, KA01A1234. "^$|" lets an empty value fall to @NotBlank only.
    public static final String VEHICLE_NUMBER = "^$|^[A-Za-z]{2}[0-9]{1,2}[A-Za-z]{0,3}[0-9]{4}$";

    // Indian mobile number: 10 digits starting with 6-9
    public static final String MOBILE_NUMBER = "^$|^[6-9][0-9]{9}$";

    public static final String FLAT_NUMBER = "^$|^[A-Za-z0-9-]{1,10}$";

    private ValidationPatterns() {
    }
}
