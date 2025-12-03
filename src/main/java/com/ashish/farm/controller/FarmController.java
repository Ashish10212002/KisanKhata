package com.ashish.farm.controller;

import com.ashish.farm.model.Farm;
import com.ashish.farm.service.FarmService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/farms")
@RequiredArgsConstructor
public class FarmController {

    private final FarmService farmService;

    // Get ONLY farms belonging to logged-in user
    @GetMapping
    public ResponseEntity<List<Farm>> getAllFarms() {
        return ResponseEntity.ok(farmService.getAllFarms());
    }

    // Create farm for the logged-in user
    @PostMapping
    public ResponseEntity<Farm> createFarm(@RequestBody Farm farm) {
        Farm created = farmService.createFarm(farm);
        return ResponseEntity.ok(created);
    }

    // Update existing farm (only if user owns it)
    @PutMapping("/{id}")
    public ResponseEntity<?> updateFarm(@PathVariable Long id, @RequestBody Farm updated) {
        Farm saved = farmService.updateFarmOwned(id, updated);
        return ResponseEntity.ok(saved);
    }
}
