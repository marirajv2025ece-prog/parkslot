package com.parkspot.repository;

import com.parkspot.entity.ParkingSlot;
import com.parkspot.entity.SlotStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ParkingSlotRepository extends JpaRepository<ParkingSlot, Long> {

    boolean existsBySlotNumber(String slotNumber);

    long countByStatus(SlotStatus status);

    List<ParkingSlot> findAllByOrderBySlotNumberAsc();

    List<ParkingSlot> findByStatusOrderBySlotNumberAsc(SlotStatus status);

    /**
     * Loads the slot with a database row lock (SELECT ... FOR UPDATE).
     * If two guards try to assign the same slot at the same moment,
     * the second one waits and then sees the updated OCCUPIED status.
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select s from ParkingSlot s where s.slotNumber = :slotNumber")
    Optional<ParkingSlot> findBySlotNumberForUpdate(@Param("slotNumber") String slotNumber);
}
