package com.amgboddel.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Requête pour créer ou modifier un semestre")
public class SemestreRequest {

    @NotNull(message = "Le numéro du semestre est obligatoire")
    @Min(value = 1, message = "Le numéro du semestre doit être au moins 1")
    @Max(value = 6, message = "Le numéro du semestre ne peut pas dépasser 6")
    @Schema(description = "Numéro du semestre (1-6)", example = "1")
    private Integer numeroSemestre;
}
