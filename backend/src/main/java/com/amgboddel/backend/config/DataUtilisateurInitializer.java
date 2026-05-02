package com.amgboddel.backend.config;

import com.amgboddel.backend.entity.Role;
import com.amgboddel.backend.entity.Utilisateur;
import com.amgboddel.backend.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

// Classe permettant d'initialiser des users en BDD via CommandLineRunner qui s'execute au démarrage de l'app
// Slf4j permet de faire un log.info pour afficher des logs dans la console sans coder tout une méthode auxiliaire
// Order permet de chosir l'ordre dans lequel les classes implémentant CommandLineRunner sont executés
@Component
@RequiredArgsConstructor
@Slf4j
@Order(1)
public class DataUtilisateurInitializer implements CommandLineRunner {

    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (utilisateurRepository.count() > 0) {
            log.info("Données utilisateurs déjà présentes, initialisation ignorée.");
            return;
        }

        Utilisateur admin = new Utilisateur();
        admin.setNomUtilisateur("Admin");
        admin.setEmail("admin@gestmaquette.fr");
        admin.setMotDePasse(passwordEncoder.encode("admin123"));
        admin.setRole(Role.ADMIN);

        Utilisateur secretaire = new Utilisateur();
        secretaire.setNomUtilisateur("secretaire");
        secretaire.setEmail("secretaire@gestmaquette.fr");
        secretaire.setMotDePasse(passwordEncoder.encode("secretaire123"));
        secretaire.setRole(Role.SECRETAIRE);

        Utilisateur responsable = new Utilisateur();
        responsable.setNomUtilisateur("responsable");
        responsable.setEmail("responsable@gestmaquette.fr");
        responsable.setMotDePasse(passwordEncoder.encode("responsable123"));
        responsable.setRole(Role.RESPONSABLE);

        utilisateurRepository.save(admin);
        utilisateurRepository.save(secretaire);
        utilisateurRepository.save(responsable);

        log.info("3 utilisateurs de test créés (admin, secrétaire, responsable)");
    }
}
