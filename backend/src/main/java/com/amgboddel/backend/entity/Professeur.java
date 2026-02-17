package com.amgboddel.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "professeur")
@Getter
@Setter
@NoArgsConstructor
public class Professeur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false)
    private String prenom;

    @Column(nullable = false, unique = true)
    private String email;

    @ManyToMany
    @JoinTable(
            name = "tag_professeur",
            joinColumns = @JoinColumn(name = "id_professeur"),
            inverseJoinColumns = @JoinColumn(name = "id_tag")
    )
    @JsonIgnoreProperties({"hibernateLazyInitializer"})
    private Set<Tag> tags = new HashSet<>();
}
