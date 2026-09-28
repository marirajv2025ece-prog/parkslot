package com.parkspot.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * A residential flat that is being visited.
 * The link to visits is stored in ParkingRecord (Many records -> One flat).
 */
@Entity
@Table(name = "flat")
public class Flat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 10)
    private String flatNumber;

    @Column(nullable = false, length = 50)
    private String residentName;

    public Flat() {
    }

    public Flat(String flatNumber, String residentName) {
        this.flatNumber = flatNumber;
        this.residentName = residentName;
    }

    public Long getId() {
        return id;
    }

    public String getFlatNumber() {
        return flatNumber;
    }

    public void setFlatNumber(String flatNumber) {
        this.flatNumber = flatNumber;
    }

    public String getResidentName() {
        return residentName;
    }

    public void setResidentName(String residentName) {
        this.residentName = residentName;
    }
}
