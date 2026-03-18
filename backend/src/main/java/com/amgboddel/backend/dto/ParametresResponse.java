package com.amgboddel.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ParametresResponse {
    private Double tarifCm;
    private Double tarifTd;
    private Double tarifTp;
    private Double budgetMax;
    private Boolean alertesEctsActives;
    private Boolean alertesPrerequisActives;
}
