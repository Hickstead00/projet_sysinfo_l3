package com.amgboddel.backend.repository;

import com.amgboddel.backend.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    boolean existsByNomTag(String nomTag);
    Optional<Tag> findByNomTag(String nomTag);   
    @Query("SELECT COUNT(*) FROM Professeur p JOIN p.tags t WHERE t.id = :id")
    long countProfesseurByTagId(Long id);

    @Query("SELECT COUNT(*) FROM UE u JOIN u.tags t WHERE t.id = :id")
    long countUeByTagId(Long id);
}
