package com.amgboddel.backend.repository;

import com.amgboddel.backend.entity.Professeur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProfesseurRepository extends JpaRepository<Professeur, Long> {
    List<Professeur> findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCase(String nom, String prenom);
    Optional<Professeur> findByEmail(String email);
    boolean existsByEmail(String email);
    List<Professeur> findByTagsId(Long tagId);
}
