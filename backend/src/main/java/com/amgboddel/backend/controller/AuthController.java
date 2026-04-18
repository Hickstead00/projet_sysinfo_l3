package com.amgboddel.backend.controller;

import com.amgboddel.backend.dto.AuthResponse;
import com.amgboddel.backend.dto.LoginRequest;
import com.amgboddel.backend.entity.Utilisateur;
import com.amgboddel.backend.repository.UtilisateurRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentification", description = "Login, logout et session utilisateur")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UtilisateurRepository utilisateurRepository;

    @PostMapping("/login")
    @Operation(summary = "Se connecter")
    @ApiResponse(responseCode = "200", description = "Connexion réussie")
    @ApiResponse(responseCode = "401", description = "Email ou mot de passe incorrect")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request, HttpServletRequest httpRequest) {
        try {
            // AuthenticationManager délègue à CustomUserDetailsService + BCrypt
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getMotDePasse())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            HttpSession session = httpRequest.getSession(true);
            session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());

            Utilisateur utilisateur = utilisateurRepository.findByEmail(request.getEmail()).orElseThrow();

            return ResponseEntity.ok(new AuthResponse(
                    utilisateur.getEmail(),
                    utilisateur.getNomUtilisateur(),
                    utilisateur.getRole()
            ));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Email ou mot de passe incorrect");
        }
    }

    @PostMapping("/logout")
    @Operation(summary = "Se déconnecter")
    @ApiResponse(responseCode = "200", description = "Déconnexion réussie")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok("Déconnexion réussie");
    }


    // Permet de récupérer un user déjà authentifié en cas de refresh de page notamment
    @GetMapping("/me")
    @Operation(summary = "Récupérer l'utilisateur connecté")
    @ApiResponse(responseCode = "200", description = "Utilisateur connecté")
    @ApiResponse(responseCode = "401", description = "Non authentifié")
    public ResponseEntity<?> me() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()
                || authentication.getPrincipal().equals("anonymousUser")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Non authentifié");
        }

        String email = authentication.getName();
        Utilisateur utilisateur = utilisateurRepository.findByEmail(email).orElseThrow();

        return ResponseEntity.ok(new AuthResponse(
                utilisateur.getEmail(),
                utilisateur.getNomUtilisateur(),
                utilisateur.getRole()
        ));
    }
}
