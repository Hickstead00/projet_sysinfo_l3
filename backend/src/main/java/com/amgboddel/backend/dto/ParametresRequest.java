package com.amgboddel.backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParametresRequest {

    @NotBlank(message = "Le nom du paramètre est obligatoire")
    private String nomParametre;

    @NotBlank(message = "La valeur du paramètre est obligatoire")
    private String valeur;
}