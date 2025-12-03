package com.ashish.farm.repository;

import com.ashish.farm.model.Farm;
import com.ashish.farm.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FarmRepository extends JpaRepository<Farm, Long> {
    List<Farm> findByUser(User user);
}
