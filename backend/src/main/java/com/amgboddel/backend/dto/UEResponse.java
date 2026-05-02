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
public class UEResponse {

    private Long id;
    private String nomUe;
    private Integer ects;
    private Integer cm;
    private Integer td;
    private Integer tp;
    private String description;
    private Boolean ueObligatoire;

    // Relations détaillées pour le front
    private List<TagResponse> tags;
    private List<ProfesseurResponse> enseignants;
    private List<ProfesseurResponse> referents;
    private List<UEResponse> prerequis;

    // dummy values pour faciler le dev front
    private int nbEnseignants;
    private int nbReferents;
    private int nbPrerequis;
    private int nbSemestres;
    private int nbMaquettes;
    private int volumeHoraireTotal;
}