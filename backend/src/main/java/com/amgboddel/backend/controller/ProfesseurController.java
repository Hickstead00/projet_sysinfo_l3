package com.amgboddel.backend.controller;

import com.amgboddel.backend.dto.ProfesseurRequest;
import com.amgboddel.backend.dto.ProfesseurResponse;
import com.amgboddel.backend.service.ProfesseurService;
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
@RequestMapping("/api/professeurs")
@RequiredArgsConstructor
@Tag(name = "Professeurs", description = "Gestion des Professeurs")
public class ProfesseurController {

    private final ProfesseurService professeurService;

    @GetMapping
    @Operation(summary = "Lister tout les professeurs")
    public ResponseEntity<List<ProfesseurResponse>> getAll(){
        return ResponseEntity.ok(professeurService.getAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un professeur par son ID")
    @ApiResponse(responseCode = "200", description = "Professeur trouvé")
    @ApiResponse(responseCode = "404", description = "Professeur non trouvé")
    public ResponseEntity<ProfesseurResponse> getById(@PathVariable Long id){
        return ResponseEntity.ok(professeurService.getById(id));
    }

    @GetMapping("/search")
    @Operation(summary = "Rechercher des professeurs par nom ou prénom")
    public ResponseEntity<List<ProfesseurResponse>> search(@RequestParam String s){
        return ResponseEntity.ok(professeurService.search(s));
    }

    @PostMapping
    @Operation(summary = "Créer un nouveau professeur")
    @ApiResponse(responseCode = "201", description = "Professeur crée")
    @ApiResponse(responseCode = "400", description = "Données invalides")
    @ApiResponse(responseCode = "409", description = "Email déjà utilisé")
    public ResponseEntity<ProfesseurResponse> create(@Valid @RequestBody ProfesseurRequest request) {
        ProfesseurResponse created = professeurService.create(request);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifier un professeur existant")
    @ApiResponse(responseCode = "201", description = "Professeur modifié")
    @ApiResponse(responseCode = "404", description = "Professeur non trouvé")
    @ApiResponse(responseCode = "409", description = "Email déjà utilisé")
    public ResponseEntity<ProfesseurResponse> update(@PathVariable Long id, @Valid @RequestBody ProfesseurRequest request) {
        return ResponseEntity.ok(professeurService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un professeur")
    @ApiResponse(responseCode = "204", description = "Professeur supprimé")
    @ApiResponse(responseCode = "404", description = "Professeur non trouvé")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        professeurService.delete(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/hourlyVolumeTeachers")
    @Operation(summary = "Calculer le nombre d'heure totale dans les UE affectés à des enseignants")
    @ApiResponse(responseCode = "200", description = "Nombre d'heure total des enseignants récupéré")
    @ApiResponse(responseCode = "404", description = "Nombre d'heure total des enseignants non récupéré")
    public ResponseEntity<Long> hourlyVolume(){ return ResponseEntity.ok(professeurService.hourlyVolumeTeachers());}

}
