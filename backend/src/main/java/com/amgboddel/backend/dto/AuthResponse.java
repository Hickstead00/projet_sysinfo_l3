package com.amgboddel.backend.dto;

import com.amgboddel.backend.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthResponse {
    private String email;
    private String nomUtilisateur;
    private Role role;
}
