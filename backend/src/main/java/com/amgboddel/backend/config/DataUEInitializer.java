package com.amgboddel.backend.config;

import com.amgboddel.backend.entity.Professeur;
import com.amgboddel.backend.entity.Tag;
import com.amgboddel.backend.entity.UE;
import com.amgboddel.backend.repository.ProfesseurRepository;
import com.amgboddel.backend.repository.TagRepository;
import com.amgboddel.backend.repository.UERepository;
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
@Order(4)
public class DataUEInitializer implements CommandLineRunner {

    private final UERepository ueRepository;
    private final TagRepository tagRepository;
    private final ProfesseurRepository professeurRepository;

    @Override
    public void run(String... args) {

        if (ueRepository.count() > 0) {
            log.info("Données UE déjà présentes, initialisation ignorée.");
            return;
        }

        List<Tag> tags = tagRepository.findAll();
        List<Professeur> profs = professeurRepository.findAll();

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

        if (tags.size() < 10 || tagManquant || profs.size() !=15) {
            log.warn("Tags ou professeurs manquants. Initialisation UE annulée.");
            return;
        }

        UE ueUn = new UE();
        ueUn.setNomUe("Java avancé");
        ueUn.setEcts(5);
        ueUn.setCm(20);
        ueUn.setTd(20);
        ueUn.setTp(20);
        ueUn.setDescription("Développement Java et Spring Boot.");
        ueUn.setUeObligatoire(true);
        ueUn.getTags().add(tags.get(0));
        ueUn.getTags().add(tags.get(1));
        ueUn.getEnseignants().add(profs.get(0));
        ueUn.getReferents().add(profs.get(1));

        UE ueDeux = new UE();
        ueDeux.setNomUe("Algorithmique et complexité");
        ueDeux.setEcts(4);
        ueDeux.setCm(18);
        ueDeux.setTd(18);
        ueDeux.setTp(24);
        ueDeux.setDescription("Étude des algorithmes et de leur complexité.");
        ueDeux.setUeObligatoire(true);
        ueDeux.getTags().add(tags.get(2));
        ueDeux.getTags().add(tags.get(3));
        ueDeux.getEnseignants().add(profs.get(2));
        ueDeux.getReferents().add(profs.get(3));

        UE ueTrois = new UE();
        ueTrois.setNomUe("Système d'informations");
        ueTrois.setEcts(4);
        ueTrois.setCm(15);
        ueTrois.setTd(15);
        ueTrois.setTp(30);
        ueTrois.setDescription("Modélisation et gestion des bases de données relationnelles.");
        ueTrois.setUeObligatoire(true);
        ueTrois.getTags().add(tags.get(9));
        ueTrois.getEnseignants().add(profs.get(4));
        ueTrois.getReferents().add(profs.get(5));

        UE ueQuatre = new UE();
        ueQuatre.setNomUe("Réseaux informatiques");
        ueQuatre.setEcts(4);
        ueQuatre.setCm(20);
        ueQuatre.setTd(10);
        ueQuatre.setTp(20);
        ueQuatre.setDescription("Introduction aux réseaux et protocoles.");
        ueQuatre.setUeObligatoire(true);
        ueQuatre.getTags().add(tags.get(7));
        ueQuatre.getEnseignants().add(profs.get(6));
        ueQuatre.getReferents().add(profs.get(7));

        UE ueCinq = new UE();
        ueCinq.setNomUe("Programmation Web");
        ueCinq.setEcts(5);
        ueCinq.setCm(20);
        ueCinq.setTd(20);
        ueCinq.setTp(20);
        ueCinq.setDescription("Développement d'applications web modernes.");
        ueCinq.setUeObligatoire(true);
        ueCinq.getTags().add(tags.get(1));
        ueCinq.getEnseignants().add(profs.get(7));
        ueCinq.getReferents().add(profs.get(8));

        UE ueSix = new UE();
        ueSix.setNomUe("Mathématiques appliquées");
        ueSix.setEcts(3);
        ueSix.setCm(25);
        ueSix.setTd(25);
        ueSix.setTp(0);
        ueSix.setDescription("Mathématiques pour l’informatique");
        ueSix.setUeObligatoire(true);
        ueSix.getTags().add(tags.get(3));
        ueSix.getEnseignants().add(profs.get(9));
        ueSix.getReferents().add(profs.get(8));

        UE ueSept = new UE();
        ueSept.setNomUe("Architecture logicielle");
        ueSept.setEcts(4);
        ueSept.setCm(18);
        ueSept.setTd(18);
        ueSept.setTp(24);
        ueSept.setDescription("Principes de conception et architectures logicielles.");
        ueSept.setUeObligatoire(true);
        ueSept.getTags().add(tags.get(0));
        ueSept.getTags().add(tags.get(4));
        ueSept.getEnseignants().add(profs.get(0));
        ueSept.getReferents().add(profs.get(1));

        UE ueHuit = new UE();
        ueHuit.setNomUe("Systèmes d'exploitation");
        ueHuit.setEcts(3);
        ueHuit.setCm(20);
        ueHuit.setTd(15);
        ueHuit.setTp(15);
        ueHuit.setDescription("Fonctionnement des OS et gestion des processus.");
        ueHuit.setUeObligatoire(true);
        ueHuit.getTags().add(tags.get(2));
        ueHuit.getTags().add(tags.get(6));
        ueHuit.getEnseignants().add(profs.get(2));
        ueHuit.getReferents().add(profs.get(3));

        UE ueNeuf = new UE();
        ueNeuf.setNomUe("Intelligence artificielle");
        ueNeuf.setEcts(1);
        ueNeuf.setCm(20);
        ueNeuf.setTd(20);
        ueNeuf.setTp(20);
        ueNeuf.setDescription("Introduction aux techniques d'IA.");
        ueNeuf.setUeObligatoire(true);
        ueNeuf.getTags().add(tags.get(1));
        ueNeuf.getTags().add(tags.get(9));
        ueNeuf.getTags().add(tags.get(3));
        ueNeuf.getEnseignants().add(profs.get(3));
        ueNeuf.getReferents().add(profs.get(4));

        UE ueDix = new UE();
        ueDix.setNomUe("Stage");
        ueDix.setEcts(0);
        ueDix.setCm(5);
        ueDix.setTd(0);
        ueDix.setTp(0);
        ueDix.setDescription("Stage professeionnelle");
        ueDix.setUeObligatoire(false);
        ueDix.getEnseignants().add(profs.get(5));
        ueDix.getReferents().add(profs.get(5));

        UE ueOnze = new UE();
        ueOnze.setNomUe("Sécurité informatique");
        ueOnze.setEcts(4);
        ueOnze.setCm(20);
        ueOnze.setTd(20);
        ueOnze.setTp(10);
        ueOnze.setDescription("Fondamentaux de la cybersécurité.");
        ueOnze.setUeObligatoire(true);
        ueOnze.getTags().add(tags.get(7));
        ueOnze.getTags().add(tags.get(8));
        ueOnze.getEnseignants().add(profs.get(10));
        ueOnze.getReferents().add(profs.get(7));

        UE ueDouze = new UE();
        ueDouze.setNomUe("Techniques de communication");
        ueDouze.setEcts(2);
        ueDouze.setCm(18);
        ueDouze.setTd(12);
        ueDouze.setTp(30);
        ueDouze.setDescription("Apprentissage de la communication de groupe");
        ueDouze.setUeObligatoire(true);
        ueDouze.getTags().add(tags.get(6));
        ueDouze.getEnseignants().add(profs.get(3));
        ueDouze.getReferents().add(profs.get(3));

        UE ueTreize = new UE();
        ueTreize.setNomUe("Anglais");
        ueTreize.setEcts(2);
        ueTreize.setCm(20);
        ueTreize.setTd(20);
        ueTreize.setTp(20);
        ueTreize.setDescription("Approfondissement d'anglais");
        ueTreize.setUeObligatoire(true);
        ueTreize.getTags().add(tags.get(6));
        ueTreize.getTags().add(tags.get(5));
        ueTreize.getEnseignants().add(profs.get(9));
        ueTreize.getReferents().add(profs.get(9));

        UE ueQuatorze = new UE();
        ueQuatorze.setNomUe("Science économique et sociale");
        ueQuatorze.setEcts(2);
        ueQuatorze.setCm(20);
        ueQuatorze.setTd(20);
        ueQuatorze.setTp(20);
        ueQuatorze.setDescription("Étude des environnement économiques");
        ueQuatorze.setUeObligatoire(true);
        ueQuatorze.getTags().add(tags.get(1));
        ueQuatorze.getTags().add(tags.get(3));
        ueQuatorze.getTags().add(tags.get(7));
        ueQuatorze.getEnseignants().add(profs.get(10));
        ueQuatorze.getReferents().add(profs.get(12));

        UE ueQuinze = new UE();
        ueQuinze.setNomUe("Management");
        ueQuinze.setEcts(3);
        ueQuinze.setCm(20);
        ueQuinze.setTd(20);
        ueQuinze.setTp(15);
        ueQuinze.setDescription("Étude des différentes méthode de management");
        ueQuinze.setUeObligatoire(true);
        ueQuinze.getTags().add(tags.get(9));
        ueQuinze.getEnseignants().add(profs.get(13));
        ueQuinze.getReferents().add(profs.get(12));

        UE ueSeize = new UE();
        ueSeize.setNomUe("Anglais 2");
        ueSeize.setEcts(2);
        ueSeize.setCm(20);
        ueSeize.setTd(20);
        ueSeize.setTp(20);
        ueSeize.setDescription("Approfondissement d'anglais en 2ème année");
        ueSeize.setUeObligatoire(true);
        ueSeize.getTags().add(tags.get(6));
        ueSeize.getTags().add(tags.get(5));
        ueSeize.getEnseignants().add(profs.get(9));
        ueSeize.getReferents().add(profs.get(9));
        ueSeize.getPrerequis().add(ueTreize);

        UE ueDixsept = new UE();
        ueDixsept.setNomUe("Anglais 3");
        ueDixsept.setEcts(2);
        ueDixsept.setCm(20);
        ueDixsept.setTd(20);
        ueDixsept.setTp(20);
        ueDixsept.setDescription("Approfondissement d'anglais en 3ème année");
        ueDixsept.setUeObligatoire(true);
        ueDixsept.getTags().add(tags.get(6));
        ueDixsept.getTags().add(tags.get(5));
        ueDixsept.getEnseignants().add(profs.get(9));
        ueDixsept.getReferents().add(profs.get(9));
        ueDixsept.getPrerequis().add(ueSeize);

        UE ueDixhuit = new UE();
        ueDixhuit.setNomUe("Statistiques");
        ueDixhuit.setEcts(4);
        ueDixhuit.setCm(20);
        ueDixhuit.setTd(30);
        ueDixhuit.setTp(5);
        ueDixhuit.setDescription("Statistiques pour l’informatique");
        ueDixhuit.setUeObligatoire(true);
        ueDixhuit.getTags().add(tags.get(3));
        ueDixhuit.getReferents().add(profs.get(8));
        ueDixhuit.getPrerequis().add(ueSix);

        UE ueDixNeuf = new UE();
        ueDixNeuf.setNomUe("Test 1");
        ueDixNeuf.setEcts(30);
        ueDixNeuf.setCm(10);
        ueDixNeuf.setTd(10);
        ueDixNeuf.setTp(10);
        ueDixNeuf.setDescription("UE de test");
        ueDixNeuf.setUeObligatoire(true);
        ueDixNeuf.getEnseignants().add(profs.get(1));
        ueDixNeuf.getReferents().add(profs.get(0));

        UE ueVingt = new UE();
        ueVingt.setNomUe("Test 2");
        ueVingt.setEcts(30);
        ueVingt.setCm(10);
        ueVingt.setTd(10);
        ueVingt.setTp(10);
        ueVingt.setDescription("UE de test");
        ueVingt.setUeObligatoire(true);
        ueVingt.getEnseignants().add(profs.get(1));
        ueVingt.getReferents().add(profs.get(0));

        UE ueVingtEtUn = new UE();
        ueVingtEtUn.setNomUe("Test 3");
        ueVingtEtUn.setEcts(30);
        ueVingtEtUn.setCm(10);
        ueVingtEtUn.setTd(10);
        ueVingtEtUn.setTp(10);
        ueVingtEtUn.setDescription("UE de test");
        ueVingtEtUn.setUeObligatoire(true);
        ueVingtEtUn.getEnseignants().add(profs.get(1));
        ueVingtEtUn.getReferents().add(profs.get(0));

        UE ueVingtDeux = new UE();
        ueVingtDeux.setNomUe("Test 4");
        ueVingtDeux.setEcts(30);
        ueVingtDeux.setCm(10);
        ueVingtDeux.setTd(10);
        ueVingtDeux.setTp(10);
        ueVingtDeux.setDescription("UE de test");
        ueVingtDeux.setUeObligatoire(true);
        ueVingtDeux.getEnseignants().add(profs.get(1));
        ueVingtDeux.getReferents().add(profs.get(0));

        UE ueVingtTrois = new UE();
        ueVingtTrois.setNomUe("Test 5");
        ueVingtTrois.setEcts(30);
        ueVingtTrois.setCm(10);
        ueVingtTrois.setTd(10);
        ueVingtTrois.setTp(10);
        ueVingtTrois.setDescription("UE de test");
        ueVingtTrois.setUeObligatoire(true);
        ueVingtTrois.getEnseignants().add(profs.get(1));
        ueVingtTrois.getReferents().add(profs.get(0));

        UE ueVingtQuatre = new UE();
        ueVingtQuatre.setNomUe("Test 6");
        ueVingtQuatre.setEcts(30);
        ueVingtQuatre.setCm(10);
        ueVingtQuatre.setTd(10);
        ueVingtQuatre.setTp(10);
        ueVingtQuatre.setDescription("UE de test");
        ueVingtQuatre.setUeObligatoire(true);
        ueVingtQuatre.getEnseignants().add(profs.get(1));
        ueVingtQuatre.getReferents().add(profs.get(0));

        ueRepository.save(ueUn);
        ueRepository.save(ueDeux);
        ueRepository.save(ueTrois);
        ueRepository.save(ueQuatre);
        ueRepository.save(ueCinq);
        ueRepository.save(ueSix);
        ueRepository.save(ueSept);
        ueRepository.save(ueHuit);
        ueRepository.save(ueNeuf);
        ueRepository.save(ueDix);
        ueRepository.save(ueOnze);
        ueRepository.save(ueDouze);
        ueRepository.save(ueTreize);
        ueRepository.save(ueQuatorze);
        ueRepository.save(ueQuinze);
        ueRepository.save(ueSeize);
        ueRepository.save(ueDixsept);
        ueRepository.save(ueDixhuit);
        ueRepository.save(ueDixNeuf);
        ueRepository.save(ueVingt);
        ueRepository.save(ueVingtEtUn);
        ueRepository.save(ueVingtDeux);
        ueRepository.save(ueVingtTrois);
        ueRepository.save(ueVingtQuatre);


        log.info("15 UE de test créées");
    }
}