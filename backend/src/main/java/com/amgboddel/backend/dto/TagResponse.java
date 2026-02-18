package com.amgboddel.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TagResponse {
    private Long id;
    private String nomTag;
    private String couleur;
}
