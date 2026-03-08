package com.amgboddel.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class SemestreResponse {

    private Long id;
    private Integer numeroSemestre;

    private List<UEResponse> ues;

    private Integer nbUe;
    private Integer ectsTotal;
    private Integer volumeHoraireCm;
    private Integer volumeHoraireTd;
    private Integer volumeHoraireTp;
    private Integer volumeHoraireTotal;

    private Boolean respecteEcts; // 30 ECTS par semestre
    private Integer ectsManquants;
    private Integer ectsSurplus;
}
