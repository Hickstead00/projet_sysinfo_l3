package com.amgboddel.backend.controller;

import com.amgboddel.backend.dto.MaquetteRequest;
import com.amgboddel.backend.dto.MaquetteResponse;
import com.amgboddel.backend.service.MaquetteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/maquettes")
@RequiredArgsConstructor
@Tag(name = "Maquette", description = "Gestion des maquettes pédagogiques")
public class MaquetteController {

    private final MaquetteService maquetteService;

    @GetMapping
    @Operation(summary = "Lister toutes les maquettes")
    @ApiResponse(responseCode = "200", description = "Liste des maquettes")
    public ResponseEntity<List<MaquetteResponse>> getAll() {
        return ResponseEntity.ok(maquetteService.getAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer une maquette par son ID")
    @ApiResponse(responseCode = "200", description = "Maquette trouvée")
    @ApiResponse(responseCode = "404", description = "Maquette non trouvée")
    public ResponseEntity<MaquetteResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(maquetteService.getById(id));
    }

    @GetMapping("/search")
    @Operation(summary = "Rechercher des maquettes par nom")
    @ApiResponse(responseCode = "200", description = "Résultats de la recherche")
    public ResponseEntity<List<MaquetteResponse>> search(@RequestParam String s) {
        return ResponseEntity.ok(maquetteService.search(s));
    }

    @PostMapping
    @Operation(summary = "Créer une nouvelle maquette")
    @ApiResponse(responseCode = "201", description = "Maquette créée avec succès")
    @ApiResponse(responseCode = "400", description = "Données invalides")
    @ApiResponse(responseCode = "409", description = "Une maquette avec ce nom existe déjà")
    public ResponseEntity<MaquetteResponse> create(@Valid @RequestBody MaquetteRequest request) {
        MaquetteResponse created = maquetteService.create(request);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifier une maquette existante")
    @ApiResponse(responseCode = "200", description = "Maquette modifiée avec succès")
    @ApiResponse(responseCode = "404", description = "Maquette non trouvée")
    @ApiResponse(responseCode = "409", description = "Conflit de données (nom déjà utilisé)")
    public ResponseEntity<MaquetteResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody MaquetteRequest request
    ) {
        return ResponseEntity.ok(maquetteService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer une maquette")
    @ApiResponse(responseCode = "204", description = "Maquette supprimée avec succès")
    @ApiResponse(responseCode = "404", description = "Maquette non trouvée")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        maquetteService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
