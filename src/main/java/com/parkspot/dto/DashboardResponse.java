package com.parkspot.dto;

import java.util.List;

public record DashboardResponse(long totalSlots, long availableSlots, long occupiedSlots, long vehiclesInside,
                                List<SlotResponse> slots, List<ParkingRecordResponse> recentVisitors) {
}
