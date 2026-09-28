package com.parkspot.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * A numbered visitor parking slot (P01 ... P10).
 * Status is AVAILABLE or OCCUPIED. It is changed only by ParkingService.
 */
@Entity
@Table(name = "parking_slot")
public class ParkingSlot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 10)
    private String slotNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15)
    private SlotStatus status = SlotStatus.AVAILABLE;

    public ParkingSlot() {
    }

    public ParkingSlot(String slotNumber) {
        this.slotNumber = slotNumber;
        this.status = SlotStatus.AVAILABLE;
    }

    public Long getId() {
        return id;
    }

    public String getSlotNumber() {
        return slotNumber;
    }

    public void setSlotNumber(String slotNumber) {
        this.slotNumber = slotNumber;
    }

    public SlotStatus getStatus() {
        return status;
    }

    public void setStatus(SlotStatus status) {
        this.status = status;
    }
}
