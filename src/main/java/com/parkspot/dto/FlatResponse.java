package com.parkspot.dto;

import com.parkspot.entity.Flat;

public record FlatResponse(Long id, String flatNumber, String residentName) {

    public static FlatResponse from(Flat flat) {
        return new FlatResponse(flat.getId(), flat.getFlatNumber(), flat.getResidentName());
    }
}
