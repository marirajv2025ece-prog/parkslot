package com.parkspot.service;

import com.parkspot.dto.FlatRequest;
import com.parkspot.dto.FlatResponse;
import com.parkspot.entity.Flat;
import com.parkspot.exception.DuplicateResourceException;
import com.parkspot.exception.ResourceNotFoundException;
import com.parkspot.repository.FlatRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class FlatService {

    private final FlatRepository flatRepository;

    public FlatService(FlatRepository flatRepository) {
        this.flatRepository = flatRepository;
    }

    @Transactional
    public FlatResponse createFlat(FlatRequest request) {
        String flatNumber = request.flatNumber().trim().toUpperCase();
        if (flatRepository.existsByFlatNumber(flatNumber)) {
            throw new DuplicateResourceException("Flat " + flatNumber + " already exists.");
        }
        Flat flat = new Flat(flatNumber, request.residentName().trim());
        return FlatResponse.from(flatRepository.save(flat));
    }

    public List<FlatResponse> getAllFlats() {
        return flatRepository.findAll().stream().map(FlatResponse::from).toList();
    }

    public FlatResponse getFlatById(Long id) {
        Flat flat = flatRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Flat with id " + id + " not found."));
        return FlatResponse.from(flat);
    }

    /** Used by ParkingService: finds the Flat entity or throws 404. */
    public Flat findEntityByNumber(String flatNumber) {
        String number = flatNumber.trim().toUpperCase();
        return flatRepository.findByFlatNumber(number)
                .orElseThrow(() -> new ResourceNotFoundException("Flat " + number + " does not exist."));
    }
}
