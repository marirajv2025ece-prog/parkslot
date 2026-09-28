package com.parkspot.service;

import com.parkspot.dto.DashboardResponse;
import com.parkspot.entity.ParkingStatus;
import com.parkspot.entity.SlotStatus;
import com.parkspot.repository.ParkingRecordRepository;
import com.parkspot.repository.ParkingSlotRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class DashboardService {

    private final ParkingSlotRepository slotRepository;
    private final ParkingRecordRepository recordRepository;
    private final SlotService slotService;
    private final ParkingService parkingService;

    public DashboardService(ParkingSlotRepository slotRepository, ParkingRecordRepository recordRepository,
                            SlotService slotService, ParkingService parkingService) {
        this.slotRepository = slotRepository;
        this.recordRepository = recordRepository;
        this.slotService = slotService;
        this.parkingService = parkingService;
    }

    public DashboardResponse getDashboard() {
        return new DashboardResponse(
                slotRepository.count(),
                slotRepository.countByStatus(SlotStatus.AVAILABLE),
                slotRepository.countByStatus(SlotStatus.OCCUPIED),
                recordRepository.countByStatus(ParkingStatus.ACTIVE),
                slotService.getAllSlots(),
                parkingService.getRecentRecords());
    }
}
