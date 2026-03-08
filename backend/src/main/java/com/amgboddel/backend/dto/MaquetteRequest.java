package com.amgboddel.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Schema(description = "Requête pour créer ou modifier une maquette pédagogique")
public class MaquetteRequest {

    @NotBlank(message = "Le nom de la maquette est obligatoire")
    @Size(max = 255, message = "Le nom de la maquette ne peut pas dépasser 255 caractères")
    @Schema(description = "Nom de la maquette", example = "Licence Informatique 2025-2026")
    private String nomMaquette;

    @Schema(description = "Liste des semestres de la maquette (4 à 6 semestres)")
    private List<SemestreRequest> semestres = new ArrayList<>();
}
