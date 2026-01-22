package com.amgboddel.backend.test;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "test_messages")
@Schema(description = "Message de test")
public class TestMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "Contenu du message", example = "Hello World")
    private String content;

    private LocalDateTime createdAt;

    public TestMessage() {
        this.createdAt = LocalDateTime.now();
    }

    public TestMessage(String content) {
        this.content = content;
        this.createdAt = LocalDateTime.now();
    }

}