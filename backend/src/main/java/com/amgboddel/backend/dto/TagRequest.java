package com.amgboddel.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TagRequest {

    @NotBlank(message = "Le nom du tag est obligatoire")
    private String nomTag;

    @NotBlank(message = "La couleur est obligatoire")
    @Pattern(regexp = "^#[0-9A-Fa-f]{6}$", message = "La couleur doit être un code héxadécimal valide")
    private String couleur;
}
