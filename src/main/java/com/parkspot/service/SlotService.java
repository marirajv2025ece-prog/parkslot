package com.parkspot.service;

import com.parkspot.dto.SlotResponse;
import com.parkspot.entity.ParkingRecord;
import com.parkspot.entity.ParkingSlot;
import com.parkspot.entity.ParkingStatus;
import com.parkspot.entity.SlotStatus;
import com.parkspot.exception.ResourceNotFoundException;
import com.parkspot.repository.ParkingRecordRepository;
import com.parkspot.repository.ParkingSlotRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class SlotService {

    private final ParkingSlotRepository slotRepository;
    private final ParkingRecordRepository recordRepository;

    public SlotService(ParkingSlotRepository slotRepository, ParkingRecordRepository recordRepository) {
        this.slotRepository = slotRepository;
        this.recordRepository = recordRepository;
    }

    public List<SlotResponse> getAllSlots() {
        return toResponses(slotRepository.findAllByOrderBySlotNumberAsc());
    }

    public List<SlotResponse> getAvailableSlots() {
        return toResponses(slotRepository.findByStatusOrderBySlotNumberAsc(SlotStatus.AVAILABLE));
    }

    public List<SlotResponse> getOccupiedSlots() {
        return toResponses(slotRepository.findByStatusOrderBySlotNumberAsc(SlotStatus.OCCUPIED));
    }

    public SlotResponse getSlotById(Long id) {
        ParkingSlot slot = slotRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Parking slot with id " + id + " not found."));
        ParkingRecord activeRecord = recordRepository
                .findByParkingSlotIdAndStatus(slot.getId(), ParkingStatus.ACTIVE).orElse(null);
        return SlotResponse.from(slot, activeRecord);
    }

    // One query for all active records, then match them to slots in memory
    private List<SlotResponse> toResponses(List<ParkingSlot> slots) {
        Map<Long, ParkingRecord> activeBySlotId = recordRepository
                .findByStatusOrderByEntryTimeDesc(ParkingStatus.ACTIVE).stream()
                .collect(Collectors.toMap(record -> record.getParkingSlot().getId(),
                        Function.identity(), (first, second) -> first));

        return slots.stream()
                .map(slot -> SlotResponse.from(slot, activeBySlotId.get(slot.getId())))
                .toList();
    }
}
