package com.amgboddel.backend.controller;

import com.amgboddel.backend.dto.ParametresRequest;
import com.amgboddel.backend.dto.ParametresResponse;
import com.amgboddel.backend.service.ParametresService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/parametres")
@RequiredArgsConstructor
@Tag(name = "Paramètres", description = "Gestion des paramètres de l'application")
public class ParametresController {

    private final ParametresService parametresService;

    @GetMapping
    @Operation(summary = "Récupérer les paramètres", description = "Retourne la configuration actuelle de l'application")
    @ApiResponse(responseCode = "200", description = "Paramètres récupérés avec succès")
    public ResponseEntity<ParametresResponse> get() {
        return ResponseEntity.ok(parametresService.get());
    }

    @PutMapping
    @Operation(summary = "Modifier les paramètres", description = "Met à jour la configuration de l'application")
    @ApiResponse(responseCode = "200", description = "Paramètres mis à jour avec succès")
    @ApiResponse(responseCode = "400", description = "Données invalides")
    public ResponseEntity<ParametresResponse> update(@Valid @RequestBody ParametresRequest request) {
        return ResponseEntity.ok(parametresService.update(request));
    }
}
