package com.parkspot.config;

import com.parkspot.entity.Flat;
import com.parkspot.entity.ParkingSlot;
import com.parkspot.entity.StaffUser;
import com.parkspot.repository.FlatRepository;
import com.parkspot.repository.ParkingSlotRepository;
import com.parkspot.repository.StaffUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Runs once at every start-up. Every step first checks whether the data already exists,
 * so restarting the application never creates duplicate slots, flats or users.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private static final int TOTAL_SLOTS = 10;
    private static final String DEFAULT_USERNAME = "admin";
    private static final String DEFAULT_PASSWORD = "admin123";

    private final ParkingSlotRepository slotRepository;
    private final FlatRepository flatRepository;
    private final StaffUserRepository staffUserRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(ParkingSlotRepository slotRepository, FlatRepository flatRepository,
                           StaffUserRepository staffUserRepository, PasswordEncoder passwordEncoder) {
        this.slotRepository = slotRepository;
        this.flatRepository = flatRepository;
        this.staffUserRepository = staffUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        createSlots();
        createSampleFlats();
        createDefaultStaffUser();
    }

    private void createSlots() {
        for (int i = 1; i <= TOTAL_SLOTS; i++) {
            String slotNumber = String.format("P%02d", i);      // P01, P02 ... P10
            if (!slotRepository.existsBySlotNumber(slotNumber)) {
                slotRepository.save(new ParkingSlot(slotNumber));
            }
        }
    }

    private void createSampleFlats() {
        Map<String, String> sampleFlats = Map.of(
                "101", "Anand Raj",
                "102", "Meena Devi",
                "103", "Karthik Subramani",
                "104", "Priya Nair",
                "105", "Ramesh Babu");

        sampleFlats.forEach((flatNumber, residentName) -> {
            if (!flatRepository.existsByFlatNumber(flatNumber)) {
                flatRepository.save(new Flat(flatNumber, residentName));
            }
        });
    }

    private void createDefaultStaffUser() {
        if (staffUserRepository.findByUsername(DEFAULT_USERNAME).isEmpty()) {
            staffUserRepository.save(new StaffUser(DEFAULT_USERNAME,
                    passwordEncoder.encode(DEFAULT_PASSWORD), "Gate Security"));
            // Do not print the default password into application logs.
            log.info("Default login created -> username: {}", DEFAULT_USERNAME);
        }
    }
}
