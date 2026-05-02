package com.amgboddel.backend.config;

import com.amgboddel.backend.entity.Tag;
import com.amgboddel.backend.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
@Order(2)
public class DataTagInitializer implements CommandLineRunner {

    private final TagRepository tagRepository;

    @Override
    public void run(String... args) {
        if (tagRepository.count() > 0) {
            log.info("Données tags déjà présentes, initialisation ignorée.");
            return;
        }

        Tag tagUn = new Tag();
        tagUn.setNomTag("Java");
        tagUn.setCouleur("#e74c3c");

        Tag tagDeux = new Tag();
        tagDeux.setNomTag("Programmation Web");
        tagDeux.setCouleur("#3498db");

        Tag tagTrois = new Tag();
        tagTrois.setNomTag("Algorithmique");
        tagTrois.setCouleur("#2ecc71");

        Tag tagQuatre = new Tag();
        tagQuatre.setNomTag("Mathématiques");
        tagQuatre.setCouleur("#f1c40f");

        Tag tagCinq = new Tag();
        tagCinq.setNomTag("Économie");
        tagCinq.setCouleur("#9b59b6");

        Tag tagSix = new Tag();
        tagSix.setNomTag("Anglais");
        tagSix.setCouleur("#e67e22");

        Tag tagSept = new Tag();
        tagSept.setNomTag("Droit");
        tagSept.setCouleur("#1abc9c");

        Tag tagHuit = new Tag();
        tagHuit.setNomTag("Réseau");
        tagHuit.setCouleur("#34495e");

        Tag tagNeuf = new Tag();
        tagNeuf.setNomTag("Communication");
        tagNeuf.setCouleur("#ff6b81");

        Tag tagDix = new Tag();
        tagDix.setNomTag("Base de données");
        tagDix.setCouleur("#7f8c8d");

        tagRepository.save(tagUn);
        tagRepository.save(tagDeux);
        tagRepository.save(tagTrois);
        tagRepository.save(tagQuatre);
        tagRepository.save(tagCinq);
        tagRepository.save(tagSix);
        tagRepository.save(tagSept);
        tagRepository.save(tagHuit);
        tagRepository.save(tagNeuf);
        tagRepository.save(tagDix);

        log.info("10 tags de test créés");
    }
}
