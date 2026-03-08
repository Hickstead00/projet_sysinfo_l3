package com.amgboddel.backend.repository;

import com.amgboddel.backend.entity.Semestre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SemestreRepository extends JpaRepository<Semestre, Long> {

    List<Semestre> findByMaquetteId(Long maquetteId);

    Optional<Semestre> findByMaquetteIdAndNumeroSemestre(Long maquetteId, Integer numeroSemestre);

    boolean existsByMaquetteIdAndNumeroSemestre(Long maquetteId, Integer numeroSemestre);
}
