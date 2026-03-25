package com.amgboddel.backend.controller;

import com.amgboddel.backend.dto.SemestreRequest;
import com.amgboddel.backend.dto.SemestreResponse;
import com.amgboddel.backend.service.SemestreService;
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
@RequestMapping("/api/maquettes/{maquetteId}/semestres")
@RequiredArgsConstructor
@Tag(name = "Semestre", description = "Gestion des semestres (routes hiérarchiques)")
public class SemestreController {

    private final SemestreService semestreService;

    @GetMapping
    @Operation(summary = "Lister tous les semestres d'une maquette")
    @ApiResponse(responseCode = "200", description = "Liste des semestres de la maquette")
    public ResponseEntity<List<SemestreResponse>> getAllByMaquette(@PathVariable Long maquetteId) {
        return ResponseEntity.ok(semestreService.getByMaquetteId(maquetteId));
    }

    @GetMapping("/{semestreId}")
    @Operation(summary = "Récupérer un semestre spécifique d'une maquette")
    @ApiResponse(responseCode = "200", description = "Semestre trouvé")
    @ApiResponse(responseCode = "404", description = "Semestre ou maquette non trouvé(e)")
    public ResponseEntity<SemestreResponse> getById(
            @PathVariable Long maquetteId,
            @PathVariable Long semestreId
    ) {
        return ResponseEntity.ok(semestreService.getById(semestreId, maquetteId));
    }

    @PostMapping
    @Operation(summary = "Créer un nouveau semestre dans une maquette")
    @ApiResponse(responseCode = "201", description = "Semestre créé avec succès")
    @ApiResponse(responseCode = "400", description = "Données invalides")
    @ApiResponse(responseCode = "404", description = "Maquette non trouvée")
    @ApiResponse(responseCode = "409", description = "Un semestre avec ce numéro existe déjà dans cette maquette")
    public ResponseEntity<SemestreResponse> create(
            @PathVariable Long maquetteId,
            @Valid @RequestBody SemestreRequest request
    ) {
        SemestreResponse created = semestreService.create(maquetteId, request);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{semestreId}")
    @Operation(summary = "Modifier un semestre existant")
    @ApiResponse(responseCode = "200", description = "Semestre modifié avec succès")
    @ApiResponse(responseCode = "404", description = "Semestre non trouvé")
    @ApiResponse(responseCode = "409", description = "Conflit de données")
    public ResponseEntity<SemestreResponse> update(
            @PathVariable Long maquetteId,
            @PathVariable Long semestreId,
            @Valid @RequestBody SemestreRequest request
    ) {
        return ResponseEntity.ok(semestreService.update(semestreId, maquetteId, request));
    }

    @DeleteMapping("/{semestreId}")
    @Operation(summary = "Supprimer un semestre")
    @ApiResponse(responseCode = "204", description = "Semestre supprimé avec succès")
    @ApiResponse(responseCode = "404", description = "Semestre non trouvé")
    public ResponseEntity<Void> delete(
            @PathVariable Long maquetteId,
            @PathVariable Long semestreId
    ) {
        semestreService.delete(semestreId, maquetteId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{semestreId}/ues/{ueId}")
    @Operation(summary = "Ajouter une UE à un semestre",
            description = "Valide que tous les prérequis directs de l'UE sont placés dans un semestre " +
                    "avec un numéro strictement inférieur au semestre cible. " +
                    "En cas d'erreur 422, le champ 'message' liste les UEs prérequises manquantes ou mal placées.")
    @ApiResponse(responseCode = "200", description = "UE ajoutée au semestre avec succès")
    @ApiResponse(responseCode = "404", description = "Semestre ou UE non trouvé(e)")
    @ApiResponse(responseCode = "409", description = "UE déjà présente dans un semestre de cette maquette")
    @ApiResponse(responseCode = "422", description = "Prérequis non respectés : une ou plusieurs UEs prérequises sont absentes ou placées dans un semestre égal ou ultérieur")
    public ResponseEntity<SemestreResponse> addUE(
            @PathVariable Long maquetteId,
            @PathVariable Long semestreId,
            @PathVariable Long ueId
    ) {
        return ResponseEntity.ok(semestreService.addUE(semestreId, maquetteId, ueId));
    }

    @DeleteMapping("/{semestreId}/ues/{ueId}")
    @Operation(summary = "Retirer une UE d'un semestre",
            description = "Bloque le retrait si une UE placée dans un semestre ultérieur de la même maquette " +
                    "a cette UE comme prérequis direct. " +
                    "En cas d'erreur 422, le champ 'message' liste les UEs dépendantes avec leur semestre.")
    @ApiResponse(responseCode = "200", description = "UE retirée du semestre avec succès")
    @ApiResponse(responseCode = "404", description = "Semestre ou UE non trouvé(e)")
    @ApiResponse(responseCode = "422", description = "Retrait impossible : des UEs placées dans des semestres ultérieurs dépendent de cette UE comme prérequis")
    public ResponseEntity<SemestreResponse> removeUE(
            @PathVariable Long maquetteId,
            @PathVariable Long semestreId,
            @PathVariable Long ueId
    ) {
        return ResponseEntity.ok(semestreService.removeUE(semestreId, maquetteId, ueId));
    }
}
