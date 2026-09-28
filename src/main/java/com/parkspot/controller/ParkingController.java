package com.parkspot.controller;

import com.parkspot.dto.DailyReportResponse;
import com.parkspot.dto.ParkingEntryRequest;
import com.parkspot.dto.ParkingRecordResponse;
import com.parkspot.service.ParkingService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/parking")
public class ParkingController {

    private final ParkingService parkingService;

    public ParkingController(ParkingService parkingService) {
        this.parkingService = parkingService;
    }

    @PostMapping("/entry")
    public ResponseEntity<ParkingRecordResponse> entry(@Valid @RequestBody ParkingEntryRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(parkingService.registerEntry(request));
    }

    @PostMapping("/exit/{id}")
    public ParkingRecordResponse exit(@PathVariable Long id) {
        return parkingService.recordExit(id);
    }

    @GetMapping("/active")
    public List<ParkingRecordResponse> active() {
        return parkingService.getActiveRecords();
    }

    @GetMapping("/history")
    public List<ParkingRecordResponse> history(
            @RequestParam(required = false) String vehicleNumber,
            @RequestParam(required = false) String flatNumber,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return parkingService.getHistory(vehicleNumber, flatNumber, date);
    }

    @GetMapping("/report/daily")
    public DailyReportResponse dailyReport(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return parkingService.getDailyReport(date);
    }
}
