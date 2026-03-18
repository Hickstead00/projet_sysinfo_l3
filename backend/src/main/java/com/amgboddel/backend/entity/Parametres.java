package com.amgboddel.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "parametres")
@Getter
@Setter
@NoArgsConstructor
public class Parametres {

    @Id
    private Long id = 1L;

    @Column(nullable = false)
    private Double tarifCm = 45.0;

    @Column(nullable = false)
    private Double tarifTd = 37.5;

    @Column(nullable = false)
    private Double tarifTp = 37.5;

    @Column(nullable = false)
    private Double budgetMax = 0.0;

    @Column(nullable = false)
    private Boolean alertesEctsActives = true;

    @Column(nullable = false)
    private Boolean alertesPrerequisActives = true;
}
