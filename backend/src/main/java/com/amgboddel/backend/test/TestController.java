package com.amgboddel.backend.test;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/test")
@CrossOrigin(origins = "http://localhost:4200")
public class TestController {

    private final TestMessageRepository repository;

    public TestController(TestMessageRepository repository) {
        this.repository = repository;
    }

    // GET - Vérifier que l'API répond
    @GetMapping("/ping")
    public ResponseEntity<Map<String, String>> ping() {
        return ResponseEntity.ok(Map.of(
                "status", "OK",
                "message", "Backend connecté !"
        ));
    }

    // GET - Récupérer tous les messages
    @GetMapping("/messages")
    @Operation(
            summary = "Récupérer tout les messages",
            description = "Retourne la liste de tous les messages stockés en BD"
    )
    @ApiResponse(responseCode = "200", description = "Ok")
    public ResponseEntity<List<TestMessage>> getAllMessages() {
        return ResponseEntity.ok(repository.findAll());
    }

    // POST - Créer un message
    @PostMapping("/messages")
    @Operation(
            summary = "Créer un message",
            description = "Créer un message et le sauvegarder en BD"
    )
    public ResponseEntity<TestMessage> createMessage(@RequestBody Map<String, String> payload) {
        String content = payload.get("content");
        TestMessage message = new TestMessage(content);
        TestMessage saved = repository.save(message);
        return ResponseEntity.ok(saved);
    }

    // DELETE - Supprimer tous les messages (pour nettoyer)
    @DeleteMapping("/messages")
    public ResponseEntity<Map<String, String>> deleteAllMessages() {
        repository.deleteAll();
        return ResponseEntity.ok(Map.of("status", "Tous les messages supprimés"));
    }
}