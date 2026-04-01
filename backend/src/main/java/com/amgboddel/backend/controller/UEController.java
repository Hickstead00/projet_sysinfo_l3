package com.amgboddel.backend.controller;


import com.amgboddel.backend.dto.UERequest;
import com.amgboddel.backend.dto.UEResponse;
import com.amgboddel.backend.service.UEService;
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
@RequestMapping("/api/ues")
@RequiredArgsConstructor
@Tag(name = "UE", description = "Gestion des Unités d'Enseignement")
public class UEController {

    private final UEService ueService;

    @GetMapping
    @Operation(summary = "Lister toutes les UE")
    public ResponseEntity<List<UEResponse>> getAll(){
        return ResponseEntity.ok(ueService.getAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer une UE par son ID")
    @ApiResponse(responseCode = "200", description = "UE trouvée")
    @ApiResponse(responseCode = "404", description = "UE non trouvée")
    public ResponseEntity<UEResponse> getById(@PathVariable Long id){
        return ResponseEntity.ok(ueService.getById(id));
    }

    @GetMapping("/search")
    @Operation(summary = "Rechercher des UE par nom")
    public ResponseEntity<List<UEResponse>> search(@RequestParam String s){
        return ResponseEntity.ok(ueService.search(s));
    }

    @PostMapping
    @Operation(summary = "Créer une nouvelle UE")
    @ApiResponse(responseCode = "201", description = "UE créée")
    @ApiResponse(responseCode = "400", description = "Données invalides")
    @ApiResponse(responseCode = "409", description = "UE déjà existante")
    public ResponseEntity<UEResponse> create(@Valid @RequestBody UERequest request) {
        UEResponse created = ueService.create(request);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifier une UE existante")
    @ApiResponse(responseCode = "200", description = "UE modifiée")
    @ApiResponse(responseCode = "404", description = "UE non trouvée")
    @ApiResponse(responseCode = "409", description = "Conflit de données")
    public ResponseEntity<UEResponse> update(@PathVariable Long id, @Valid @RequestBody UERequest request) {
        return ResponseEntity.ok(ueService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer une UE")
    @ApiResponse(responseCode = "204", description = "UE supprimée")
    @ApiResponse(responseCode = "404", description = "UE non trouvée")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        ueService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/count")
    @Operation(summary = "Caluler le nombre total d'UE")
    @ApiResponse(responseCode = "200", description = "Nombre d'UE récupéré")
    @ApiResponse(responseCode = "404", description = "Nombre d'UE non récupéré")
    public ResponseEntity<Long> count() {
        return ResponseEntity.ok(ueService.count());
    }

    @GetMapping("/countUEattachToTeachers")
    @Operation(summary = "Caluler le nombre total d'UE")
    @ApiResponse(responseCode = "200", description = "Nombre d'UE attachés à des enseignants récupéré")
    @ApiResponse(responseCode = "404", description = "Nombre d'UE attachés à des enseignants non récupéré")
    public ResponseEntity<Long> countUEattachToTeachers() {
        return ResponseEntity.ok(ueService.countUEattachToTeachers());
    }

    @GetMapping("/hourlyVolume")
    @Operation(summary = "Calculer le nombre d'heure totale dans les UE")
    @ApiResponse(responseCode = "200", description = "Nombre d'heure total récupéré")
    @ApiResponse(responseCode = "404", description = "Nombre d'heure total non récupéré")
    public ResponseEntity<Integer> hourlyVolume(){ return ResponseEntity.ok(ueService.hourlyVolume());}

}