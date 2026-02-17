package com.amgboddel.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "parametres")
@Getter
@Setter
@NoArgsConstructor
public class Parametres {

    @Id
    @Column(name = "nom_parametre")
    private String nomParametre;

    @Column(nullable = false)
    private String valeur;
}
