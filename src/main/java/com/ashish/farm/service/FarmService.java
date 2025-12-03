package com.ashish.farm.service;

import com.ashish.farm.model.Farm;
import com.ashish.farm.model.User;
import com.ashish.farm.repository.FarmRepository;
import com.ashish.farm.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FarmService {
    private final FarmRepository farmRepository;
    private final UserRepository userRepository;

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public List<Farm> getAllFarms() {
        User current = getCurrentUser();
        return farmRepository.findByUser(current);
    }

    public Farm createFarm(Farm farm) {
        User current = getCurrentUser();
        farm.setUser(current);
        return farmRepository.save(farm);
    }

    public Optional<Farm> getById(Long id) {
        return farmRepository.findById(id);
    }

    public Farm updateFarmOwned(Long id, Farm updated) {
        User current = getCurrentUser();
        Farm existing = farmRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Farm not found"));
        if (existing.getUser() == null || !existing.getUser().getId().equals(current.getId())) {
            throw new RuntimeException("Not authorized to update this farm");
        }
        existing.setName(updated.getName());
        existing.setCrop(updated.getCrop());
        existing.setArea(updated.getArea());
        existing.setSowingDate(updated.getSowingDate());
        existing.setLocation(updated.getLocation());
        return farmRepository.save(existing);
    }
}
