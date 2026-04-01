package com.amgboddel.backend.repository;

import com.amgboddel.backend.dto.ProfesseurStatsProjection;
import com.amgboddel.backend.entity.UE;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UERepository extends JpaRepository<UE, Long> {

    List<UE> findByNomUeContainingIgnoreCase(String nomUe);
    boolean existsByNomUe(String nomUe);
    List<UE> findByTagsId(Long tagId);
    List<UE> findByEnseignantsId(Long professeurId);
    List<UE> findByReferentsId(Long professeurId);

    @Query(value = """
            SELECT
                COALESCE(SUM(u.cm), 0)               AS totalCm,
                COALESCE(SUM(u.td), 0)               AS totalTd,
                COALESCE(SUM(u.tp), 0)               AS totalTp,
                COALESCE(SUM(u.cm + u.td + u.tp), 0) AS totalHeures,
                COUNT(u.id)                           AS nbUe
            FROM ue u
            JOIN enseigne e ON e.id_ue = u.id
            WHERE e.id_professeur = :profId
            """, nativeQuery = true)
    ProfesseurStatsProjection findStatsByEnseignantId(@Param("profId") Long profId);

    @Query(value = """
            SELECT COUNT(u.id)
            FROM ue u
            JOIN referent r ON r.id_ue = u.id
            WHERE r.id_professeur = :profId
            """, nativeQuery = true)
    Long countByReferentId(@Param("profId") Long profId);

    Long countDistinctByEnseignantsIsNotEmpty();
}