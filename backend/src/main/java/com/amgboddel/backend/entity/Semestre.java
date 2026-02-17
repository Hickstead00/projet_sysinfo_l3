package com.amgboddel.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "semestre")
@Getter
@Setter
@NoArgsConstructor
public class Semestre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer numeroSemestre;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_maquette", nullable = false)
    @JsonIgnoreProperties("semestres")
    private Maquette maquette;

    @ManyToMany
    @JoinTable(
            name = "semestre_ue",
            joinColumns = @JoinColumn(name = "id_semestre"),
            inverseJoinColumns = @JoinColumn(name = "id_ue")
    )
    @JsonIgnoreProperties({"semestres", "prerequis", "enseignants", "referents"})
    private Set<UE> ues = new HashSet<>();
}
