package com.parkspot.dto;

import com.parkspot.entity.ParkingRecord;
import com.parkspot.entity.ParkingSlot;
import com.parkspot.entity.SlotStatus;

import java.time.LocalDateTime;

/** A slot plus (when occupied) the vehicle that is currently using it. */
public record SlotResponse(Long id, String slotNumber, SlotStatus status, Long parkingRecordId,
                           String vehicleNumber, String flatNumber, LocalDateTime entryTime) {

    public static SlotResponse from(ParkingSlot slot, ParkingRecord activeRecord) {
        if (activeRecord == null) {
            return new SlotResponse(slot.getId(), slot.getSlotNumber(), slot.getStatus(), null, null, null, null);
        }
        return new SlotResponse(slot.getId(), slot.getSlotNumber(), slot.getStatus(), activeRecord.getId(),
                activeRecord.getVisitorVehicle().getVehicleNumber(),
                activeRecord.getFlat().getFlatNumber(),
                activeRecord.getEntryTime());
    }
}
