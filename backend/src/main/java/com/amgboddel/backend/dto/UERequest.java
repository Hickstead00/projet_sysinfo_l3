package com.amgboddel.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UeRequest {

    @NotBlank(message = "Le nom de l'UE est obligatoire")
    @Size(max = 150, message = "Le nom de l'UE ne peut pas dépasser 150 caractères")
    private String nomUe;

    @NotNull(message = "Le nombre d'ECTS est obligatoire")
    @Min(value = 0, message = "Les ECTS doivent être positifs")
    private Integer ects;

    @NotNull(message = "Le nombre d'heures CM est obligatoire")
    @Min(value = 0, message = "Les heures CM doivent être positives")
    private Integer cm;

    @NotNull(message = "Le nombre d'heures TD est obligatoire")
    @Min(value = 0, message = "Les heures TD doivent être positives")
    private Integer td;

    @NotNull(message = "Le nombre d'heures TP est obligatoire")
    @Min(value = 0, message = "Les heures TP doivent être positives")
    private Integer tp;

    @Schema(description = "Description détaillée de l'UE")
    private String description;

    @NotNull(message = "Le statut obligatoire est requis")
    private Boolean ueObligatoire;

    @Schema(description = "Liste des IDs des tags associés à l'UE", example = "[1,3,12]")
    private List<Long> tagIds;

    @Schema(description = "Liste des IDs des enseignants", example = "[2,5]")
    private List<Long> enseignantIds;

    @Schema(description = "Liste des IDs des référents", example = "[4]")
    private List<Long> referentIds;

    @Schema(description = "Liste des IDs des prérequis", example = "[10,11]")
    private List<Long> prerequisIds;
}