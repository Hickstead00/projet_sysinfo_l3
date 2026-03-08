package com.amgboddel.backend.repository;

import com.amgboddel.backend.entity.Maquette;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MaquetteRepository extends JpaRepository<Maquette, Long> {

    List<Maquette> findByNomMaquetteContainingIgnoreCase(String nomMaquette);

    boolean existsByNomMaquette(String nomMaquette);
}
