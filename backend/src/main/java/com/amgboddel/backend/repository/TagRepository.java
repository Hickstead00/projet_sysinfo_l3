package com.amgboddel.backend.repository;

import com.amgboddel.backend.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    boolean existsByNomTag(String nomTag);
    Optional<Tag> findByNomTag(String nomTag);
    long countProfesseursByTags_Id(Long id);
    long countUesByTags_Id(Long id);
}
