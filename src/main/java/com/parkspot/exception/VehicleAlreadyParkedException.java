package com.parkspot.exception;

public class VehicleAlreadyParkedException extends RuntimeException {

    public VehicleAlreadyParkedException(String message) {
        super(message);
    }
}
