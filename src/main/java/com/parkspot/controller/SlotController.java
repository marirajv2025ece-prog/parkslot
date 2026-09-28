package com.parkspot.controller;

import com.parkspot.dto.SlotResponse;
import com.parkspot.service.SlotService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/slots")
public class SlotController {

    private final SlotService slotService;

    public SlotController(SlotService slotService) {
        this.slotService = slotService;
    }

    @GetMapping
    public List<SlotResponse> getAll() {
        return slotService.getAllSlots();
    }

    @GetMapping("/available")
    public List<SlotResponse> getAvailable() {
        return slotService.getAvailableSlots();
    }

    @GetMapping("/occupied")
    public List<SlotResponse> getOccupied() {
        return slotService.getOccupiedSlots();
    }

    @GetMapping("/{id}")
    public SlotResponse getById(@PathVariable Long id) {
        return slotService.getSlotById(id);
    }
}
