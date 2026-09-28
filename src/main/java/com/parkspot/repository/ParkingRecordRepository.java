package com.parkspot.repository;

import com.parkspot.entity.ParkingRecord;
import com.parkspot.entity.ParkingStatus;
import com.parkspot.entity.VisitorVehicle;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ParkingRecordRepository extends JpaRepository<ParkingRecord, Long> {

    boolean existsByVisitorVehicleAndStatus(VisitorVehicle visitorVehicle, ParkingStatus status);

    boolean existsByVisitorVehicleId(Long vehicleId);

    long countByStatus(ParkingStatus status);

    Optional<ParkingRecord> findByParkingSlotIdAndStatus(Long slotId, ParkingStatus status);

    // @EntityGraph loads vehicle, flat and slot in the same query (avoids many small queries)
    @EntityGraph("ParkingRecord.details")
    List<ParkingRecord> findByStatusOrderByEntryTimeDesc(ParkingStatus status);

    @EntityGraph("ParkingRecord.details")
    List<ParkingRecord> findByEntryTimeBetweenOrderByEntryTimeAsc(LocalDateTime start, LocalDateTime end);

    @EntityGraph("ParkingRecord.details")
    List<ParkingRecord> findTop5ByOrderByEntryTimeDesc();

    /**
     * Search filter for parking history.
     * An empty string ("") for vehicleNumber / flatNumber means "do not filter on this".
     * fromTime / toTime is the entry-time range (the whole day when a date is chosen).
     */
    @EntityGraph("ParkingRecord.details")
    @Query("""
            select r from ParkingRecord r
            where r.status = :status
              and upper(r.visitorVehicle.vehicleNumber) like concat('%', :vehicleNumber, '%')
              and (:flatNumber = '' or r.flat.flatNumber = :flatNumber)
              and r.entryTime >= :fromTime
              and r.entryTime <= :toTime
            order by r.entryTime desc
            """)
    List<ParkingRecord> searchByStatus(@Param("status") ParkingStatus status,
                                       @Param("vehicleNumber") String vehicleNumber,
                                       @Param("flatNumber") String flatNumber,
                                       @Param("fromTime") LocalDateTime fromTime,
                                       @Param("toTime") LocalDateTime toTime);
}
