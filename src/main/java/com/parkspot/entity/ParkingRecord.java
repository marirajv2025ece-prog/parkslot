package com.parkspot.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

/**
 * One visit = one ParkingRecord. It stores the full entry/exit history.
 * Many records can point to the same vehicle, flat or slot (Many-To-One),
 * but only one ACTIVE record can exist for a slot or a vehicle at a time.
 */
@Entity
@Table(name = "parking_record")
@NamedEntityGraph(
        name = "ParkingRecord.details",
        attributeNodes = {
                @NamedAttributeNode("visitorVehicle"),
                @NamedAttributeNode("flat"),
                @NamedAttributeNode("parkingSlot")
        })
public class ParkingRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "vehicle_id", nullable = false)
    private VisitorVehicle visitorVehicle;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "flat_id", nullable = false)
    private Flat flat;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "slot_id", nullable = false)
    private ParkingSlot parkingSlot;

    @Column(length = 100)
    private String purpose;

    // Rule 2: entry time is mandatory. Exit time stays null until the vehicle leaves.
    @Column(nullable = false)
    private LocalDateTime entryTime;

    private LocalDateTime exitTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15)
    private ParkingStatus status = ParkingStatus.ACTIVE;

    public ParkingRecord() {
    }

    public Long getId() {
        return id;
    }

    public VisitorVehicle getVisitorVehicle() {
        return visitorVehicle;
    }

    public void setVisitorVehicle(VisitorVehicle visitorVehicle) {
        this.visitorVehicle = visitorVehicle;
    }

    public Flat getFlat() {
        return flat;
    }

    public void setFlat(Flat flat) {
        this.flat = flat;
    }

    public ParkingSlot getParkingSlot() {
        return parkingSlot;
    }

    public void setParkingSlot(ParkingSlot parkingSlot) {
        this.parkingSlot = parkingSlot;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public LocalDateTime getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(LocalDateTime entryTime) {
        this.entryTime = entryTime;
    }

    public LocalDateTime getExitTime() {
        return exitTime;
    }

    public void setExitTime(LocalDateTime exitTime) {
        this.exitTime = exitTime;
    }

    public ParkingStatus getStatus() {
        return status;
    }

    public void setStatus(ParkingStatus status) {
        this.status = status;
    }
}
