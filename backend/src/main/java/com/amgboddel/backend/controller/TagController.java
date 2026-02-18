package com.amgboddel.backend.controller;

import com.amgboddel.backend.dto.TagRequest;
import com.amgboddel.backend.dto.TagResponse;
import com.amgboddel.backend.service.TagService;
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
@RequestMapping("/api/tags")
@RequiredArgsConstructor
@Tag(name = "Tags", description = "Gestion des tags de compétence")
public class TagController {

    private final TagService tagService;

    @GetMapping
    @Operation(summary = "Lister tout les tags", description = "Retourne la liste de tout les tags")
    public ResponseEntity<List<TagResponse>> getAll(){
        return ResponseEntity.ok(tagService.getAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un tag par son ID")
    @ApiResponse(responseCode = "200", description = "Tag trouvé")
    @ApiResponse(responseCode = "404", description = "Tag introuvable")
    public ResponseEntity<TagResponse> getById(@PathVariable Long id){
        return ResponseEntity.ok(tagService.getById(id));
    }

    @PostMapping
    @Operation(summary = "Créer un nouveau tag", description = "Crée un tag avec un nom unique et une couleur héxad. ")
    @ApiResponse(responseCode = "201", description = "Tag crée avec succès")
    @ApiResponse(responseCode = "400", description = "Données invalides")
    @ApiResponse(responseCode = "409", description = "Un tag avec ce nom existe déjà")
    public ResponseEntity<TagResponse> create(@Valid @RequestBody TagRequest request){
        TagResponse created = tagService.create(request);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifier un tag existant")
    @ApiResponse(responseCode = "200", description = "Tag modifié avec succès")
    @ApiResponse(responseCode = "404", description = "Tag introuvable")
    @ApiResponse(responseCode = "409", description = "Un tag avec ce nom existe déjà")
    public ResponseEntity<TagResponse> update(@PathVariable Long id, @Valid @RequestBody TagRequest request){
        return ResponseEntity.ok(tagService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un tag")
    @ApiResponse(responseCode = "204", description = "Tag supprimé avec succès")
    @ApiResponse(responseCode = "404", description = "Tag introuvable")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        tagService.delete(id);
        return ResponseEntity.noContent().build();
    }


}
