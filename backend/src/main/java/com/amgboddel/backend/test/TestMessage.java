package com.amgboddel.backend.test;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "test_messages")
public class TestMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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