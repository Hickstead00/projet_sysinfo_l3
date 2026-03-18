package com.amgboddel.backend.repository;

import com.amgboddel.backend.entity.Parametres;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParametresRepository extends JpaRepository<Parametres, Long> {
}
