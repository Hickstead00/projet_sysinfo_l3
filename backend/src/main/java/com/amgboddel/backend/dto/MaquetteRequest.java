package com.amgboddel.backend.dto;

import com.amgboddel.backend.entity.TypeMaquette;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Requête pour créer ou modifier une maquette pédagogique")
public class MaquetteRequest {

    @NotBlank(message = "Le nom de la maquette est obligatoire")
    @Size(max = 255, message = "Le nom de la maquette ne peut pas dépasser 255 caractères")
    @Schema(description = "Nom de la maquette", example = "Licence Informatique 2025-2026")
    private String nomMaquette;

    @NotNull(message = "Le type de maquette est obligatoire")
    @Schema(description = "Type de maquette : LICENCE (6 semestres) ou MASTER (4 semestres)", example = "LICENCE")
    private TypeMaquette typeMaquette;
}
