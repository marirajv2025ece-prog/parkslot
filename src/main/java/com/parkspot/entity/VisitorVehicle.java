package com.parkspot.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;

/**
 * A vehicle that visits the society. One vehicle can visit many times,
 * so it can have many ParkingRecords (One-To-Many).
 */
@Entity
@Table(name = "visitor_vehicle")
public class VisitorVehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 15)
    private String vehicleNumber;

    @Column(nullable = false, length = 20)
    private String vehicleType;

    @Column(nullable = false, length = 50)
    private String visitorName;

    @Column(nullable = false, length = 10)
    private String mobileNumber;

    // inverse side: the foreign key lives in parking_record.vehicle_id
    @OneToMany(mappedBy = "visitorVehicle")
    private List<ParkingRecord> parkingRecords = new ArrayList<>();

    public VisitorVehicle() {
    }

    public Long getId() {
        return id;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getVisitorName() {
        return visitorName;
    }

    public void setVisitorName(String visitorName) {
        this.visitorName = visitorName;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public List<ParkingRecord> getParkingRecords() {
        return parkingRecords;
    }
}
