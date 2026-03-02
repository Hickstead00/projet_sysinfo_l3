package com.amgboddel.backend.repository;

import com.amgboddel.backend.entity.UE;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UERepository extends JpaRepository<UE, Long> {

    List<UE> findByNomUeContainingIgnoreCase(String nomUe);
    boolean existsByNomUe(String nomUe);
}