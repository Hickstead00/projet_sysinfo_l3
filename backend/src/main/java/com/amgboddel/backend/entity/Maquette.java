package com.amgboddel.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "maquette")
@Getter
@Setter
@NoArgsConstructor
public class Maquette {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nomMaquette;

    @OneToMany(mappedBy = "maquette", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("maquette")
    private List<Semestre> semestres = new ArrayList<>();
}
