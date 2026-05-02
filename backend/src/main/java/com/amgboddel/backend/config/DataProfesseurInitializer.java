package com.amgboddel.backend.config;


import com.amgboddel.backend.entity.Professeur;
import com.amgboddel.backend.entity.Tag;
import com.amgboddel.backend.repository.ProfesseurRepository;
import com.amgboddel.backend.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
@Order(3)
public class DataProfesseurInitializer implements CommandLineRunner {

    private final ProfesseurRepository professeurRepository;
    private final TagRepository tagRepository;

    @Override
    public void run(String... args) {
        if (professeurRepository.count() > 0) {
            log.info("Données professeurs déjà présentes, initialisation ignorée.");
            return;
        }

        List<Tag> tags = tagRepository.findAll();
        boolean tagManquant = false;

        List<String> requiredTags = List.of(
                "Java",
                "Programmation Web",
                "Algorithmique",
                "Mathématiques",
                "Économie",
                "Anglais",
                "Droit",
                "Réseau",
                "Communication",
                "Base de données"
        );

        List<String> nomTags = new ArrayList<>();

        for (Tag tag : tags) {
            nomTags.add(tag.getNomTag());
        }

        for (String required : requiredTags) {
            if (!nomTags.contains(required)) {
                tagManquant = true;
            }
        }

        if ((tags.size() < 10) || (tagManquant))  {
            log.warn("Tags manquants. Initialisation annulée.");
            return;
        }

        Professeur pUn = new Professeur();
        pUn.setNom("Dupont");
        pUn.setPrenom("Jean");
        pUn.setEmail("jean.dupont@gmail.com");
        pUn.getTags().add(tags.get(0));
        pUn.getTags().add(tags.get(1));

        Professeur pDeux = new Professeur();
        pDeux.setNom("Martin");
        pDeux.setPrenom("Claire");
        pDeux.setEmail("claire.martin@gmail.com");
        pDeux.getTags().add(tags.get(2));
        pDeux.getTags().add(tags.get(3));

        Professeur pTrois = new Professeur();
        pTrois.setNom("Durand");
        pTrois.setPrenom("Paul");
        pTrois.setEmail("paul.durand@gmail.com");
        pTrois.getTags().add(tags.get(6));

        Professeur pQuatre = new Professeur();
        pQuatre.setNom("Dutronc");
        pQuatre.setPrenom("Luc");
        pQuatre.setEmail("luc.Dutronc@gmail.com");

        Professeur pCinq = new Professeur();
        pCinq.setNom("Bernard");
        pCinq.setPrenom("Claire");
        pCinq.setEmail("claire.bernard@gmail.com");
        pCinq.getTags().add(tags.get(0));
        pCinq.getTags().add(tags.get(7));

        Professeur pSix = new Professeur();
        pSix.setNom("Petit");
        pSix.setPrenom("Julien");
        pSix.setEmail("julien.petit@gmail.com");
        pSix.getTags().add(tags.get(1));
        pSix.getTags().add(tags.get(9));

        Professeur pSept = new Professeur();
        pSept.setNom("Robert");
        pSept.setPrenom("Sophie");
        pSept.setEmail("sophie.robert@gmail.com");
        pSept.getTags().add(tags.get(4));

        Professeur pHuit = new Professeur();
        pHuit.setNom("Richard");
        pHuit.setPrenom("Emma");
        pHuit.setEmail("emma.richard@gmail.com");
        pHuit.getTags().add(tags.get(6));
        pHuit.getTags().add(tags.get(8));

        Professeur pNeuf = new Professeur();
        pNeuf.setNom("Pourquoi");
        pNeuf.setPrenom("Hugo");
        pNeuf.setEmail("hugo.pourquoi@gmail.com");
        pNeuf.getTags().add(tags.get(5));

        Professeur pDix = new Professeur();
        pDix.setNom("Moulin");
        pDix.setPrenom("Alice");
        pDix.setEmail("alice.mouli@gmail.com");
        pDix.getTags().add(tags.get(0));
        pDix.getTags().add(tags.get(2));
        pDix.getTags().add(tags.get(9));

        Professeur pOnze = new Professeur();
        pOnze.setNom("Simon");
        pOnze.setPrenom("Nathan");
        pOnze.setEmail("nathan.simon@gmail.com");
        pOnze.getTags().add(tags.get(3));

        Professeur pDouze = new Professeur();
        pDouze.setNom("Laurent");
        pDouze.setPrenom("Chloé");
        pDouze.setEmail("chloe.laurent@gmail.com");
        pDouze.getTags().add(tags.get(1));
        pDouze.getTags().add(tags.get(6));

        Professeur pTreize = new Professeur();
        pTreize.setNom("Michel");
        pTreize.setPrenom("Lucas");
        pTreize.setEmail("lucas.michel@gmail.com");
        pTreize.getTags().add(tags.get(7));
        pTreize.getTags().add(tags.get(8));
        pTreize.getTags().add(tags.get(0));

        Professeur pQuatorze = new Professeur();
        pQuatorze.setNom("Roux");
        pQuatorze.setPrenom("Manon");
        pQuatorze.setEmail("manon.roux@gmail.com");
        pQuatorze.getTags().add(tags.get(2));

        Professeur pQuinze = new Professeur();
        pQuinze.setNom("Roux");
        pQuinze.setPrenom("Adam");
        pQuinze.setEmail("adam.roux@gmail.com");
        pQuinze.getTags().add(tags.get(5));
        pQuinze.getTags().add(tags.get(9));


        professeurRepository.save(pUn);
        professeurRepository.save(pDeux);
        professeurRepository.save(pTrois);
        professeurRepository.save(pQuatre);
        professeurRepository.save(pCinq);
        professeurRepository.save(pSix);
        professeurRepository.save(pSept);
        professeurRepository.save(pHuit);
        professeurRepository.save(pNeuf);
        professeurRepository.save(pDix);
        professeurRepository.save(pOnze);
        professeurRepository.save(pDouze);
        professeurRepository.save(pTreize);
        professeurRepository.save(pQuatorze);
        professeurRepository.save(pQuinze);

        log.info("15 professeurs de test créés");
    }
}
