package com.amgboddel.backend.repository;

import com.amgboddel.backend.entity.Parametres;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParametresRepository extends JpaRepository<Parametres, String> {
}