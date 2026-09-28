package com.parkspot.exception;

public class SlotAlreadyOccupiedException extends RuntimeException {

    public SlotAlreadyOccupiedException(String message) {
        super(message);
    }
}
