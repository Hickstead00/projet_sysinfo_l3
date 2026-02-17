package com.amgboddel.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "ue")
@Getter
@Setter
@NoArgsConstructor
public class UE {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nomUe;

    @Column(nullable = false)
    private Integer ects;

    @Column(nullable = false)
    private Integer cm = 0;

    @Column(nullable = false)
    private Integer td = 0;

    @Column(nullable = false)
    private Integer tp = 0;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private Boolean ueObligatoire = true;

    @ManyToMany
    @JoinTable(
            name = "tag_ue",
            joinColumns = @JoinColumn(name = "id_ue"),
            inverseJoinColumns = @JoinColumn(name = "id_tag")
    )
    @JsonIgnoreProperties({"hibernateLazyInitializer"})
    private Set<Tag> tags = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "enseigne",
            joinColumns = @JoinColumn(name = "id_ue"),
            inverseJoinColumns = @JoinColumn(name = "id_professeur")
    )
    @JsonIgnoreProperties({"tags"})
    private Set<Professeur> enseignants = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "referent",
            joinColumns = @JoinColumn(name = "id_ue"),
            inverseJoinColumns = @JoinColumn(name = "id_professeur")
    )
    @JsonIgnoreProperties({"tags"})
    private Set<Professeur> referents = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "prerequis_ue",
            joinColumns = @JoinColumn(name = "id_ue"),
            inverseJoinColumns = @JoinColumn(name = "id_prerequis")
    )
    @JsonIgnoreProperties({"prerequis", "enseignants", "referents", "tags", "semestres"})
    private Set<UE> prerequis = new HashSet<>();

    @ManyToMany(mappedBy = "ues")
    @JsonIgnoreProperties({"ues", "maquette"})
    private Set<Semestre> semestres = new HashSet<>();
}
