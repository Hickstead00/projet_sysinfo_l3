package com.amgboddel.backend.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParametresRequest {

    @NotNull(message = "Le tarif CM est obligatoire")
    @PositiveOrZero(message = "Le tarif CM doit être positif ou nul")
    private Double tarifCm;

    @NotNull(message = "Le tarif TD est obligatoire")
    @PositiveOrZero(message = "Le tarif TD doit être positif ou nul")
    private Double tarifTd;

    @NotNull(message = "Le tarif TP est obligatoire")
    @PositiveOrZero(message = "Le tarif TP doit être positif ou nul")
    private Double tarifTp;

    @NotNull(message = "Le budget maximum est obligatoire")
    @PositiveOrZero(message = "Le budget maximum doit être positif ou nul")
    private Double budgetMax;

    @NotNull(message = "L'activation des alertes ECTS est obligatoire")
    private Boolean alertesEctsActives;

    @NotNull(message = "L'activation des alertes prérequis est obligatoire")
    private Boolean alertesPrerequisActives;
}
