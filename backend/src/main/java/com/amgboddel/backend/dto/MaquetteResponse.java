package com.amgboddel.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class MaquetteResponse {

    private Long id;
    private String nomMaquette;

    private List<SemestreResponse> semestres;

    private Integer nbSemestres;
    private Integer ectsTotal;
    private Integer volumeHoraireTotal;
    private Integer nbUeTotal;

    private Boolean respecteEcts; // 60 ECTS par année
    private Integer ectsManquants;
    private Integer ectsSurplus;

    // Validation budgétaire
    private Double coutEstime;
    private Double budgetMax;
    private Boolean budgetDepasse;
}
