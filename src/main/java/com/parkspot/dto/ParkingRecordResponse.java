package com.parkspot.dto;

import com.parkspot.entity.ParkingRecord;
import com.parkspot.entity.ParkingStatus;

import java.time.LocalDateTime;

public record ParkingRecordResponse(Long id, String vehicleNumber, String visitorName, String flatNumber,
                                    String slotNumber, String purpose, LocalDateTime entryTime,
                                    LocalDateTime exitTime, ParkingStatus status) {

    public static ParkingRecordResponse from(ParkingRecord record) {
        return new ParkingRecordResponse(
                record.getId(),
                record.getVisitorVehicle().getVehicleNumber(),
                record.getVisitorVehicle().getVisitorName(),
                record.getFlat().getFlatNumber(),
                record.getParkingSlot().getSlotNumber(),
                record.getPurpose(),
                record.getEntryTime(),
                record.getExitTime(),
                record.getStatus());
    }
}
