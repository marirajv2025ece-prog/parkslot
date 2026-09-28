package com.parkspot.dto;

import java.time.LocalDate;
import java.util.List;

public record DailyReportResponse(LocalDate date, long totalVisitors, long completedVisits,
                                  long currentlyInside, List<ParkingRecordResponse> records) {
}
