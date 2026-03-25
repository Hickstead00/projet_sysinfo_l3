package com.amgboddel.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
@Schema(description = "Représentation complète d'une maquette avec ses indicateurs calculés. " +
        "Rappeler GET /api/maquettes/{id} après chaque ajout ou retrait d'UE pour mettre à jour le récapitulatif.")
public class MaquetteResponse {

    @Schema(description = "Identifiant unique de la maquette")
    private Long id;

    @Schema(description = "Nom de la maquette (ex: Licence Informatique MIAGE)")
    private String nomMaquette;

    @Schema(description = "Liste des semestres de la maquette avec leurs UEs")
    private List<SemestreResponse> semestres;

    @Schema(description = "Nombre total de semestres dans la maquette")
    private Integer nbSemestres;

    @Schema(description = "Total des crédits ECTS de toutes les UEs placées dans la maquette")
    private Integer ectsTotal;

    @Schema(description = "Volume horaire total (CM + TD + TP) de toutes les UEs placées")
    private Integer volumeHoraireTotal;

    @Schema(description = "Nombre total d'UEs placées dans la maquette")
    private Integer nbUeTotal;

    @Schema(description = "true si le total ECTS respecte la règle des 60 ECTS par année (30 par semestre)")
    private Boolean respecteEcts;

    @Schema(description = "Nombre d'ECTS manquants pour atteindre l'objectif. 0 si l'objectif est atteint ou dépassé")
    private Integer ectsManquants;

    @Schema(description = "Nombre d'ECTS en surplus au-delà de l'objectif. 0 si l'objectif n'est pas atteint")
    private Integer ectsSurplus;

    @Schema(description = "Coût estimé de la maquette en euros, calculé à partir des tarifs CM/TD/TP configurés dans les paramètres")
    private Double coutEstime;

    @Schema(description = "Budget maximum configuré dans les paramètres. 0 signifie qu'aucun budget n'a été défini")
    private Double budgetMax;

    @Schema(description = "true si le coût estimé dépasse le budget maximum. Toujours false si budgetMax est à 0")
    private Boolean budgetDepasse;
}
