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
public class ProfesseurResponse {
    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private List<TagResponse> tags; // Liste des tags associé au professeur

    // A partir d'ici dummy values pour faciler le dev front -> les valeurs réelles seront calculées plus tard

    private int nbUe;
    private int nbReferent;
    private int totalCm;
    private int totalTp;
    private int totalTd;
}
