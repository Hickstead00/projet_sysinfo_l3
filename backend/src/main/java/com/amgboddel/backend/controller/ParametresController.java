package com.amgboddel.backend.controller;

import com.amgboddel.backend.dto.ParametresRequest;
import com.amgboddel.backend.dto.ParametresResponse;
import com.amgboddel.backend.service.ParametresService;
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
@RequestMapping("/api/parametres")
@RequiredArgsConstructor
@Tag(name = "Paramètres", description = "Gestion des paramètres de l'application")
public class ParametresController {

    private final ParametresService parametresService;

    @GetMapping
    @Operation(summary = "Lister tous les paramètres", description = "Retourne la liste de tous les paramètres")
    public ResponseEntity<List<ParametresResponse>> getAll() {
        return ResponseEntity.ok(parametresService.getAll());
    }

    @GetMapping("/{nomParametre}")
    @Operation(summary = "Récupérer un paramètre par son nom")
    @ApiResponse(responseCode = "200", description = "Paramètre trouvé")
    @ApiResponse(responseCode = "404", description = "Paramètre introuvable")
    public ResponseEntity<ParametresResponse> getByName(@PathVariable String nomParametre) {
        return ResponseEntity.ok(parametresService.getByName(nomParametre));
    }

    @PostMapping
    @Operation(summary = "Créer un nouveau paramètre", description = "Crée un paramètre avec un nom unique et une valeur")
    @ApiResponse(responseCode = "201", description = "Paramètre créé avec succès")
    @ApiResponse(responseCode = "400", description = "Données invalides")
    @ApiResponse(responseCode = "409", description = "Un paramètre avec ce nom existe déjà")
    public ResponseEntity<ParametresResponse> create(@Valid @RequestBody ParametresRequest request) {
        ParametresResponse created = parametresService.create(request);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{nomParametre}")
    @Operation(summary = "Modifier un paramètre existant")
    @ApiResponse(responseCode = "200", description = "Paramètre modifié avec succès")
    @ApiResponse(responseCode = "404", description = "Paramètre introuvable")
    @ApiResponse(responseCode = "409", description = "Un paramètre avec ce nom existe déjà")
    public ResponseEntity<ParametresResponse> update(@PathVariable String nomParametre, @Valid @RequestBody ParametresRequest request) {
        return ResponseEntity.ok(parametresService.update(nomParametre, request));
    }

    @DeleteMapping("/{nomParametre}")
    @Operation(summary = "Supprimer un paramètre")
    @ApiResponse(responseCode = "204", description = "Paramètre supprimé avec succès")
    @ApiResponse(responseCode = "404", description = "Paramètre introuvable")
    public ResponseEntity<Void> delete(@PathVariable String nomParametre) {
        parametresService.delete(nomParametre);
        return ResponseEntity.noContent().build();
    }
}