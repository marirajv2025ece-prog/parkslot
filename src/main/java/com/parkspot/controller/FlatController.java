package com.parkspot.controller;

import com.parkspot.dto.FlatRequest;
import com.parkspot.dto.FlatResponse;
import com.parkspot.service.FlatService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/flats")
public class FlatController {

    private final FlatService flatService;

    public FlatController(FlatService flatService) {
        this.flatService = flatService;
    }

    @PostMapping
    public ResponseEntity<FlatResponse> create(@Valid @RequestBody FlatRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(flatService.createFlat(request));
    }

    @GetMapping
    public List<FlatResponse> getAll() {
        return flatService.getAllFlats();
    }

    @GetMapping("/{id}")
    public FlatResponse getById(@PathVariable Long id) {
        return flatService.getFlatById(id);
    }
}
