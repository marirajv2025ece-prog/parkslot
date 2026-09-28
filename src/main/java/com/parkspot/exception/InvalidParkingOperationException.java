package com.parkspot.exception;

public class InvalidParkingOperationException extends RuntimeException {

    public InvalidParkingOperationException(String message) {
        super(message);
    }
}
